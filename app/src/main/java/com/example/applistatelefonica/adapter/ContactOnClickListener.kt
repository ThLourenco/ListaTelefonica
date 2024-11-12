package com.example.applistatelefonica.adapter

import com.example.applistatelefonica.model.ContactModel

class ContactOnClickListener(val clickListener: (contact: ContactModel) -> Unit) {
    fun onClick(contact: ContactModel) = clickListener
}
/*Este código define uma classe ContactOnClickListener em Kotlin. Ela é usada para criar um objeto que encapsula
 um comportamento de clique, ou seja, o que deve acontecer quando um contato for clicado. Vamos detalhar cada parte do código:

Componentes do código
class ContactOnClickListener(val clickListener: (contact: ContactModel) -> Unit):

Define uma classe chamada ContactOnClickListener.
O construtor recebe um parâmetro clickListener, que é uma função lambda.
(contact: ContactModel) -> Unit: representa uma função que aceita um ContactModel como parâmetro e retorna Unit (ou seja, não retorna nada). O Unit é o equivalente ao void em outras linguagens.
val clickListener torna essa função acessível como uma propriedade da classe ContactOnClickListener.
fun onClick(contact: ContactModel) = clickListener(contact):

Define uma função onClick dentro da classe.
Essa função é chamada quando o contato é clicado.
onClick recebe um objeto ContactModel e o passa para a função clickListener, que executa o comportamento de clique definido no momento da criação do objeto ContactOnClickListener.*/