(ns yes-she-codes.cliente
  (:require [yes-she-codes.arquivo :as ysc.arquivo]))

(def repositorio-de-cliente(atom []))

(defrecord Cliente [ID Nome CPF Email])

(defn insere-cliente
  "criar uma estrutura de compra"
  [clientes cliente]
  (let [id (inc (count clientes))
        cliente-inserir (assoc cliente :ID id)]
    (conj clientes cliente-inserir)))

(defn insere-compra!
  "criar uma estrutura de compra"
  [clientes cliente]
  (swap! clientes insere-cliente cliente))

(defn novo-cliente
  "criar uma estrutura de cliente"
  [nome cpf email]
    {:Nome nome
    :CPF cpf
    :Email email})

(defn adiciona-cliente
  "Adicionar um novo cliente"
  [item]
  (novo-cliente (get item 0 "") (get item 1 "") (get item 2 "")))

(defn lista-clientes
  "Lista os clientes"
  []
  (map adiciona-cliente (ysc.arquivo/carrega-csv "clientes.csv")))

(defn lista-clientes!
  "Lista os clientes"
  [clientes lista-clientes]
  (doseq [cliente lista-clientes]
    (swap! clientes insere-cliente cliente)))

(defn pesquisa-cliente-por-id
  "Buscar uma lista de comprar por um mes especifico"
  [clientes id]
  (first (filter #(= id (get %1 :ID)) clientes)))

(defn pesquisa-cliente-por-id!
  "Buscar uma lista de comprar por um mes especifico"
  [clientes id]
  (pesquisa-cliente-por-id @clientes id))

(defn exclue-cliente
  [clientes id]
  (let [index (.indexOf clientes (pesquisa-cliente-por-id clientes id))]
    (concat (subvec clientes 0 index)
            (subvec clientes (inc index)))))

(defn exclue-cliente!
  [clientes id]
  (swap! clientes exclue-cliente id))