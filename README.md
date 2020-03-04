# Avisi Apps Formatter

This is the default formatter for all Avisi Apps projects.
It is not configurable. It uses [zprint](https://github.com/kkinnear/zprint)
under the hood.

# Getting started

## Add aliases
Add `fmt` to your project, add the following to your `deps.edn`

```clojure
{:aliases {:lint {:override-deps {avisi-apps/fmt {:git/url "git@github.com:avisi-apps/fmt.git"
                                                  :sha "<git SHA>"}}
                  :main-opts ["-m" "avisi-apps.fmt.main" "check"]}
           :fix {:main-opts ["-m" "avisi-apps.fmt.main" "fix"]}}}
```

## Check formatting

```shell script
clj -A:lint
```

## Fix formatting

```shell script
clj -A:lint:fix
```

With all the commands you can optionally give a path as a argument for example:

```shell script
clj -A:lint:fix ../src
```

# Ideas for the future

* Integrate this with a git commit hook, where we can possibly only lint changed files.
* Make sure that the Intellij configuration is exactly the same.
* Make some kind of Intellij integration where you can format a file with a shortcut.
* Package this project as a GraalVM binary which you can run superfast.
