(ns reducely.db
  (:require [clojure.java.jdbc :as sql]))

(def dev-db "postgresql://localhost:5432/reducely")

(defn create-db []
  (sql/db-do-commands dev-db
                      (sql/create-table-ddl :urls
                                            [:url :text :NOT :NULL]
                                            [:short :serial :primary :key
                                             :NOT :NULL])))

(defn add-url [url]
  (let [result (sql/insert! dev-db :urls {:url url})]
    (:short (first result))))

(defn find-url [id]
  (let [result (sql/query dev-db [(str "select * from urls where short = " id)])]
    (:url (first result))))
