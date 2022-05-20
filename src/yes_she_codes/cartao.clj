(ns yes-she-codes.cartao
  (:require [java-time :as t]
            [yes-she-codes.arquivo :as ysc.arquivo]
            [yes-she-codes.funcoes :as ysc.funcoes]))

(def repositorio-de-cartao(atom []))

(defrecord Cartao [ID Numero CVV Validade Limite IDCliente])

(defn insere-cartao
  "Insere um novo cartão no array"
  [cartoes cartao]
  (let [id (if-not (empty? cartoes)
             (+ 1 (apply max (map :ID cartoes)))
             1)
        cartao-inserir (assoc cartao :ID id)]
    (conj cartoes cartao-inserir)))

(defn insere-cartao!
  "Insere um novo cartão no array presitindo o valor"
  [cartoes cartao]
  (swap! cartoes insere-cartao cartao))

(defn novo-cartao
  "criar uma estrutura de cartao"
  [numero cvv validade limite cliente clientes]
  {:Numero (Long/valueOf (clojure.string/replace numero #" " ""))
   :CVV (Long/valueOf cvv)
   :Validade (t/local-date (str validade "-01"))
   :Limite (read-string limite)
   :IDCliente (get (ysc.funcoes/pesquisa-cliente-por-cpf cliente clientes) :ID)})

(defn adiciona-cartao
  "Adicionar um novo cartão"
  [item clientes]
  (novo-cartao (get item 0 "") (get item 1 "") (get item 2 "") (get item 3 "") (get item 4 "") clientes)
  )

(defn lista-cartoes
  "Lista os cartões"
  [clientes]
  (map #(adiciona-cartao %1 clientes) (ysc.arquivo/carrega-csv "cartoes.csv")))

(defn lista-cartoes!
  "Lista os cartões no atomo"
  [cartoes lista-cartoes]
  (doseq [cartao lista-cartoes]
    (swap! cartoes insere-cartao cartao)))

(defn pesquisa-cartao-por-id
  "Pesquisa por um id dentro da lista de cartões"
  [cartoes id]
  (first (filter #(= id (get %1 :ID)) cartoes)))

(defn pesquisa-cartao-por-id!
  "Pesquisa por um id dentro do atomo de lista de cartões"
  [cartoes id]
  (pesquisa-cartao-por-id @cartoes id))

(defn exclue-cartao
  [cartoes id]
  (let [index (.indexOf cartoes (pesquisa-cartao-por-id cartoes id))]
    (concat (subvec cartoes 0 index)
            (subvec cartoes (inc index)))))

(defn exclue-cartao!
  [cartoes id]
  (swap! cartoes exclue-cartao id))