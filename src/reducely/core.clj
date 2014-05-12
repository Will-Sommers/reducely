(ns reducely.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as mp]
            [hiccup.core :as h]
            [formative.core :as f]
            [formative.parse :as fp]
            [clojure.pprint :as pp]))


(def form
  {:fields [{:name :url}]
   :validations [[:required [:url]]]
   :action "/create"})

(defn handler [req]
  (do (pp/pprint req)
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (h/html (f/render-form form))}))

(def app
  (mp/wrap-params handler))

(defn boot []
  (jetty/run-jetty #'app {:port 8080 :join? false}))
