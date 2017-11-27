(ns fpwd.core)

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])
(defn str->int [str] (Integer. str))
(def conversions {:name identity :glitter-index str->int})

(defn convert [vamp-key value] ((get conversions vamp-key) value))


(defn append-to-suspects [name, glitter-index]

  (def s (str (str name ",") (str glitter-index ) "\n"))
  (def parsed (slurp filename))

  (if (.contains parsed s) "Suspect is already persisted in DB!"
        (spit filename s :append true)
      )
)

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn parse-n
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",") string))


(defn mapify [rows] 
  (into #{} (map (fn [unmapped-row] 
                   (reduce (fn [row-map [vamp-key value]] 
                             (assoc row-map vamp-key (convert vamp-key value)))
                           {}
                           (map vector vamp-keys unmapped-row)))
                 rows)))

(defn glitter-filter [minimum-glitter records] 
  (filter #(>= (:glitter-index %) minimum-glitter) records))

