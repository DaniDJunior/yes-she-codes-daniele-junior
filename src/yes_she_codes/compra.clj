(ns yes-she-codes.compra
  (:use [clojure pprint])
  (:require [java-time :as t]
            [yes-she-codes.arquivo :as ysc.arquivo]
            [yes-she-codes.funcoes :as ysc.funcoes]))

(def repositorio-de-compras(atom []))

(defrecord Compra [ID Data Valor Estabelecimento Categoria IDCartao])

(defn compra-valida?
  "Verifica se a compra é valida"
  [compra]
  (let [valida-Data (t/after? (t/local-date) (get compra :Data))
        valida-Valor (and (number? (get compra :Valor)) (>= (get compra :Valor) 0))
        valida-Estabelecimento (>= (count (get compra :Estabelecimento)) 2)
        categoria (get compra :Categoria)
        valida-Categoria (or (= "Alimentação" categoria)
                             (= "Automóvel" categoria)
                             (= "Casa" categoria)
                             (= "Educação" categoria)
                             (= "Lazer" categoria)
                             (= "Saúde" categoria))]
    (and valida-Data valida-Valor valida-Estabelecimento valida-Categoria)
    ))

(defn insere-compra
  "criar uma estrutura de compra"
  [compras compra]
  (if (compra-valida? compra)
  (let [id (if-not (empty? compras)
             (+ 1 (apply max (map :ID compras)))
             1)
        compra-inserir (assoc compra :ID id)]
    (conj compras compra-inserir))
  (throw (ex-info "A compra não é valida" { :tentando-inserir-compra compra }))))

(defn insere-compra!
  "criar uma estrutura de compra"
  [compras compra]
    (swap! compras insere-compra compra))

(defn novo-compra
  "criar uma estrutura de compra"
  [data valor estabelecimento categoria cartao cartoes]
  {:Data (t/local-date data)
   :Valor (bigdec valor)
   :Estabelecimento estabelecimento
   :Categoria categoria
   :IDCartao (get (ysc.funcoes/pesquisa-cartao-por-numero
                  (Long/valueOf (clojure.string/replace cartao #" " "")) cartoes)
                :ID)})

(defn adiciona-compra
  "Adicionar um novo cliente"
  [item cartoes]
  (novo-compra (get item 0 "") (get item 1 "") (get item 2 "") (get item 3 "") (get item 4 "") cartoes))

(defn lista-compras
  "Lista os clientes"
  [cartoes]
  (mapv #(adiciona-compra %1 cartoes) (ysc.arquivo/carrega-csv "compras.csv")))

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