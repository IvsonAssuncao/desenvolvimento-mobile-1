
interface ComportamentoResposta {
    fun responda(frase: String): String
}

// Classe robô Marciano
class Marciano : ComportamentoResposta {
    override fun responda(frase: String): String {
        val fraseMinuscula = frase.toLowerCase()
        val fraseMaiuscula = frase.toUpperCase()

        return when {
            frase.isEmpty() -> "Não me incomode"
            frase == fraseMaiuscula && frase.any { it.isLetter() } -> "Opa! Calma aí!"
            frase.endsWith("?") && frase == fraseMaiuscula -> "Relaxa, eu sei o que estou fazendo!"
            frase.contains("eu", ignoreCase = true) -> "A responsabilidade é sua"
            frase.endsWith("?") -> "Certamente"
            else -> "Tudo bem, como quiser"
        }
    }
}


interface AcaoUsuario {
    fun executarAcao()
}


interface ComportamentoRespostaPremium {
    fun responda(acao: String, vararg operandos: Int, acaoUsuario: AcaoUsuario?): String
}

// Classe Marciano premium
class MarcianoPremium : ComportamentoRespostaPremium {
    override fun responda(acao: String, vararg operandos: Int, acaoUsuario: AcaoUsuario?): String {
        val acaoMat = acao.toLowerCase()

        return when {
            acaoMat == "agir" -> {
                acaoUsuario?.executarAcao()
                "É pra já!"
            }
            acaoMat == "some" || acaoMat == "subtraia" ||
            acaoMat == "multiplique" || acaoMat == "divida" -> {
                val resultado = if (operandos.isNotEmpty()) {
                    when (acaoMat) {
                        "some" -> operandos.reduce { acc, i -> acc + i }
                        "subtraia" -> operandos.reduce { acc, i -> acc - i }
                        "multiplique" -> operandos.reduce { acc, i -> acc * i }
                        "divida" -> operandos.reduce { acc, i -> acc / i }
                        else -> null
                    }
                } else {
                    null
                }
                if (resultado != null) {
                    "Essa eu sei, o resultado é $resultado"
                } else {
                    "Algo deu errado. Tente novamente."
                }
            }
            else -> "Não entendi. Repita, por favor."
        }
    }
}

fun main() {
    val roboMarciano = Marciano()
    val roboMarcianoPremium = MarcianoPremium()

    val acaoPersonalizada: AcaoUsuario = object : AcaoUsuario {
        override fun executarAcao() {
            println("Executando ação personalizada! Aqui está uma mensagem especial.")
        }
    }

    // Interagir ate digite "FIM"
    var userInput: String
    do {
        print("Você: ")
        userInput = readLine() ?: ""

        if (userInput == "FIM") {
            println("Até mais! Encerrando o programa.")
            break
        }

        val userInputSplit = userInput.split(" ")
        val acao = userInputSplit[0]
        val parametros = userInputSplit.drop(1).mapNotNull { it.toIntOrNull() }.toIntArray()

        val resposta = if (acao == "agir" || acao == "some" || acao == "subtraia" || acao == "multiplique" || acao == "divida") {
            roboMarcianoPremium.responda(acao, *parametros, acaoUsuario = acaoPersonalizada)
        } else {
            roboMarciano.responda(userInput)
        }
        
        println("Robô Marciano: $resposta")
    } while (true)
}
