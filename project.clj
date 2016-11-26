(defproject mycotrack-frame "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [reagent "0.5.1"]
                 [re-frame "0.7.0"]
                 [re-com "0.8.0"]
                 [secretary "1.2.3"]
                 [garden "1.3.2"]
                 [cljs-ajax "0.5.8"]
                 [alandipert/storage-atom "2.0.1"]
                 [compojure "1.5.0"]
                 [ring "1.4.0"]
                 [cljsjs/moment "2.10.6-4"]
                 [reagent-utils "0.2.0"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.0-6"]
            [lein-garden "0.2.6"]
            [lein-doo "0.1.6"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"
                                    "resources/public/css/compiled"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler mycotrack-frame.handler/dev-handler}

  :garden {:builds [{:id "screen"
                     :source-paths ["src/clj"]
                     :stylesheet mycotrack-frame.css/screen
                     :compiler {:output-to "resources/public/css/compiled/screen.css"
                                :pretty-print? true}}]}

  :uberjar-name "mycotrack-frame.jar"
  :auto-clean false

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel {:on-jsload "mycotrack-frame.core/mount-root"}
                        :compiler {:main mycotrack-frame.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "test"
                        :source-paths ["src/cljs" "test/cljs"]
                        :compiler {:output-to "resources/public/js/compiled/test.js"
                                   :main mycotrack-frame.runner
                                   :optimizations :none}}

                       {:id "min"
                        :source-paths ["src/cljs"]
                        :jar true
                        :compiler {:main mycotrack-frame.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :closure-defines {goog.DEBUG false}
                                   :pretty-print false}}]}
  :main mycotrack-frame.server

  :aot [mycotrack-frame.server]

  :prep-tasks [["cljsbuild" "once" "min"] "compile"]

  :aliases
  {"package" ["do" "clean" ["garden" "once"] "uberjar"]})
