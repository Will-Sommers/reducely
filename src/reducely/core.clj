(ns reducely.core
  (:require [ring.adapter.jetty :as jetty]
            [hiccup.core :as h]
            [formative.core :as f]))

(def form
  {:fields [{:name :url}]
   :validations [[:required [:url]]]
   :action "/yar"})

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (h/html (f/render-form form))})

(defn boot []
  (jetty/run-jetty #'handler {:port 8080 :join? false}))
