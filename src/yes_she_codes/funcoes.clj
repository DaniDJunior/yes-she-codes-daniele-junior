(ns yes-she-codes.funcoes
  (:require [java-time :as t]))

(defn total-gasto
  "Total de gastos de uma lista de compras"
  [compras]
  (reduce + (map :Valor compras)))

(defn mes-certo?
  "É o mes certo"
  [mes compra]
  (let [intMes (t/as (get compra :Data) :month-of-year)]
    (= intMes mes)))

(defn pesquisa-compra-por-mes
  "Buscar uma lista de comprar por um mes especifico"
  [mes compras]
  (filter #(mes-certo? mes %1) compras))

(defn pesquisa-compra-por-estabelecimento
  "Buscar uma lista de comprar por um mes especifico"
  [estabelecimento compras]
  (filter #(= estabelecimento (get %1 :Estabelecimento)) compras))

(defn pesquisa-compra-por-id-cartao
  "Buscar uma lista de comprar por um mes especifico"
  [idcartao compras]
  (filter #(= idcartao (get %1 :IDCartao)) compras))

(defn total-gasto-no-mes
  "Total de gastos em um mes"
  [mes compras]
  (total-gasto (pesquisa-compra-por-mes mes compras)))

(defn pesquisa-compra-por-intervalo-valor
  "Buscar uma lista de comprar por um mes especifico"
  [valor-minimo valor-maximo compras]
  (filter #(<= valor-minimo (get %1 :Valor) valor-maximo) compras))

(defn agrupar-categoria
  "Agrupar as comprar por gategoria"
  [Compras]
  (->> Compras
       (group-by :Categoria)
       (map (fn [[chave compras]] {:Categoria chave :Valor (reduce + (map :Valor compras))}))))

(defn pesquisa-cliente-por-nome
  "Buscar uma lista de comprar por um mes especifico"
  [nome clientes]
  (filter #(= nome (get %1 :Nome)) clientes))

(defn pesquisa-cliente-por-cpf
  "Buscar uma lista de comprar por um mes especifico"
  [cpf clientes]
  (first (filter #(= cpf (get %1 :CPF)) clientes)))

(defn pesquisa-cartao-por-numero
  "Buscar uma lista de comprar por um mes especifico"
  [numero cartoes]
  (first (filter #(= numero (get %1 :Numero)) cartoes)))