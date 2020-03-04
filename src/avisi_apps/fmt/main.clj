(ns avisi-apps.fmt.main
  (:require
    [zprint.core :as zp]
    [progrock.core :as progress]
    [zprint.config]
    [zprint.main]
    clansi
    [me.raynes.fs :as fs])
  (:import
    [java.io File]))

(defn print! [bar] (progress/print bar {:format "|:bar| :progress/:total" :complete \#}))

(def options
  {:style :no-hang
   :width 120
   :map {:comma? false :indent 2}
   :fn-map
     {"defsc" :arg2
      "defn" :arg2
      ">defn" :arg2
      ">def" :arg1
      "defrouter" :arg2
      "defstatemachine" :arg1
      "pc/defmutation" :arg2
      "pc/defresolver" :arg2
      "defmutation" :arg2-extend}})

(defn format-one-file [f]
  (let [parent-dir (fs/parent f)
        tmp-name (fs/temp-name "zprint")
        tmp-file (str parent-dir File/separator tmp-name)]
    (zp/zprint-file f (fs/base-name f) tmp-file options)
    (fs/rename tmp-file f)))

(defn file-formatted? [f]
  (let [original-file-str (slurp f)
        formatted-file-str (zp/zprint-file-str original-file-str (str f) options)]
    (= original-file-str formatted-file-str)))

(defn run-with-progress! [f items]
  (let [bar (progress/progress-bar (count items))]
    (print! bar)
    (->
      (reduce
        (fn [acc item]
          (let [errors (f item)
                {:keys [bar] :as acc} (update acc :bar progress/tick)]
            (print! bar)
            (if (seq errors) (reduced (assoc acc :errors errors)) acc)))
        {:bar bar :errors nil}
        items)
      (update
        :bar
        #(->
            %
            progress/done
            print!))
      :errors)))

(defn fix [files]
  (run-with-progress!
    (fn fix-file [f]
      (try
        (format-one-file f)
        nil
        (catch Exception e
          [(str (clansi/style "Failed formatting file: " :red) (clansi/style (str f) :underline))
           (str (clansi/style "Error was: " :red) (.getMessage ^Exception e))])))
    files))

(defn check [files]
  (run-with-progress!
    (fn check-file [f]
      (when-not (file-formatted? f)
        [(str (clansi/style "File not correctly formatted: " :red) (clansi/style (str f) :underline))
         (clansi/style "Please run: 'clj -A:lint:fix' " :red)]))
    files))

(defn cleanup [] (flush) (shutdown-agents))

(defn find-files [override-folder]
  (let [folder (if override-folder override-folder ".")] (fs/find-files folder #".*\.clj[cs]?")))

(defn -main [& args]
  (zp/set-options!
    (assoc options
      :configured? true
      :parallel? true))
  (try
    (let [files (find-files (second args))
          err-msgs (case (first args)
                     "fix" (fix files)
                     "check" (check files))]
      (if err-msgs (run! println err-msgs) (println (str (clansi/style "All lights green" :green)))))
    (finally (cleanup))))

(comment
  ;; Simply find all files
  (fs/find-files "." #".*\.clj[cs]?"))
