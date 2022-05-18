(ns yes-she-codes.compra
  (:use [clojure pprint])
  (:require [java-time :as t]
            [yes-she-codes.arquivo :as ysc.arquivo]))

(def repositorio-de-compras(atom []))

(defrecord Compra [ID Data Valor Estabelecimento Categoria Cartao])

(defn insere-compra
  "criar uma estrutura de compra"
  [compras compra]
  (let [id (inc (count compras))
        compra-inserir (assoc compra :ID id)]
    (conj compras compra-inserir)))

(defn insere-compra!
  "criar uma estrutura de compra"
  [compras compra]
    (swap! compras insere-compra compra))

(defn novo-compra
  "criar uma estrutura de compra"
  [data valor estabelecimento categoria cartao]
  {:Data (t/local-date data)
   :Valor (bigdec valor)
   :Estabelecimento estabelecimento
   :Categoria categoria
   :Cartao (Long/valueOf (clojure.string/replace cartao #" " ""))})

(defn adiciona-compra
  "Adicionar um novo cliente"
  [item]
  (novo-compra (get item 0 "") (get item 1 "") (get item 2 "") (get item 3 "") (get item 4 "")))

(defn lista-compras
  "Lista os clientes"
  []
  (mapv adiciona-compra (ysc.arquivo/carrega-csv "compras.csv")))

(defn lista-compras!
  "Lista os clientes"
  [compras lista-compras]
  (doseq [compra lista-compras]
    (swap! compras insere-compra compra)))

(defn pesquisa-compra-por-id
  "Buscar uma lista de comprar por um mes especifico"
  [compras id]
  (first (filter #(= id (get %1 :ID)) compras)))

(defn pesquisa-compra-por-id!
  "Buscar uma lista de comprar por um mes especifico"
  [compras id]
  (pesquisa-compra-por-id @compras id))

(defn exclue-compra
  [compras id]
  (let [index (.indexOf compras (pesquisa-compra-por-id compras id))]
    (concat (subvec compras 0 index)
            (subvec compras (inc index)))))

(defn exclue-compra!
  [compras id]
  (swap! compras exclue-compra id))