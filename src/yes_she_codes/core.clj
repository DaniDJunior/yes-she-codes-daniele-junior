(ns yes-she-codes.core
  (:use [clojure pprint])
  (:require [yes-she-codes.cliente :as ysc.cliente]
            [yes-she-codes.cartao :as ysc.cartao]
            [yes-she-codes.compra :as ysc.compra]
            [yes-she-codes.funcoes :as ysc.funcoes]))

;(println clientes)
;(println cartoes)
;(println compras)

(ysc.cliente/lista-clientes! ysc.cliente/repositorio-de-cliente (ysc.cliente/lista-clientes))

(pprint ysc.cliente/repositorio-de-cliente)

(ysc.cartao/lista-cartoes! ysc.cartao/repositorio-de-cartao (ysc.cartao/lista-cartoes @ysc.cliente/repositorio-de-cliente))

(pprint ysc.cartao/repositorio-de-cartao)

(ysc.compra/lista-compras! ysc.compra/repositorio-de-compras (ysc.compra/lista-compras @ysc.cartao/repositorio-de-cartao))

(pprint ysc.compra/repositorio-de-compras)