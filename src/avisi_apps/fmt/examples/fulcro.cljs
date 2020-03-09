(ns avisi-apps.fmt.examples.fulcro)

(defmutation update-topdesk-config [{:keys [changed-fields]}]
  (action [{:keys [state ref]}] (swap! state assoc-in (conj ref :ui/loading?) true))
  (remote [_]
    (eql/query->ast1 `[(~'atlassian-connect.host/update-topdesk-configuration {:changed-fields ~changed-fields})]))
  (ok-action [{:keys [ref state]}]
    (swap! state
      (fn [st]
        (->
          st
          (assoc-in (conj ref :ui/loading?) false)
          (fs/entity->pristine* form-ident)))))
  (error-action [{:keys [ref state]}] (swap! state assoc-in (conj ref :ui/loading?) false)))
