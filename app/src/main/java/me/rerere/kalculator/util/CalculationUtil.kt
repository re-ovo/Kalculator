/**
 * Kotlin版计算器解析库，可以方便的计算表达式的值
 *
 * 使用`eval`函数进行调用
 *
 * 注意:
 * 本库未经过严格的测试，可能存在一些bug, 欢迎反馈
 */

package me.rerere.kalculator.util

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

// Token类型
internal enum class TokenType {
    NUMBER, // 数字
    PLUS, // 加
    MINUS, // 减
    MULTIPLY, // 乘
    DIVIDE, // 除
    NEGATIVE, // 负号
    POWER, // 指数
    LEFT_PAREN, // 左括号
    RIGHT_PAREN, // 右括号
    PI, // PI
    E, // E
    FACTORIAL, // 阶乘
    LOG, // 对数
    LN, // 自然对数
    SIN, // 正弦
    COS, // 余弦
    TAN, // 正切
    ASIN, // 反正弦
    ACOS, // 反余弦
    ATAN, // 反正切
    SQRT, // 平方根
    ABS, // 绝对值
    RAND, // 随机数
    ANGLE, // 角度
}

// Token
internal data class Token(val type: TokenType, val value: String) {
    override fun toString(): String {
        return "$type:`$value`"
    }
}

// 词法分析器
internal fun tokenize(input: String): List<Token> {
    val tokens = mutableListOf<Token>()
    var i = 0
    while (i < input.length) {
        val char = input[i]
        when {
            ' ' == char || '\n' == char -> {
                i++
            }

            char.isDigit() -> {
                var number = char.toString()
                while (i + 1 < input.length && input[i + 1].isDigit()) {
                    number += input[i + 1]
                    i++
                }
                if (i + 1 < input.length && input[i + 1] == '.') {
                    number += input[i + 1]
                    i++
                    while (i + 1 < input.length && input[i + 1].isDigit()) {
                        number += input[i + 1]
                        i++
                    }
                }
                tokens.add(Token(TokenType.NUMBER, number))
                i++
            }

            char == '+' -> {
                tokens.add(Token(TokenType.PLUS, char.toString()))
                i++
            }

            char == '-' -> {
                if (i == 0 || input[i - 1] == '(') {
                    tokens.add(Token(TokenType.NEGATIVE, char.toString()))
                } else {
                    tokens.add(Token(TokenType.MINUS, char.toString()))
                }
                i++
            }

            char == '*' || char == 'x' || char == '×' -> {
                tokens.add(Token(TokenType.MULTIPLY, char.toString()))
                i++
            }

            char == '/' || char == '÷' -> {
                tokens.add(Token(TokenType.DIVIDE, char.toString()))
                i++
            }

            char == '^' -> {
                tokens.add(Token(TokenType.POWER, char.toString()))
                i++
            }

            char == '(' -> {
                tokens.add(Token(TokenType.LEFT_PAREN, char.toString()))
                i++
            }

            char == ')' -> {
                tokens.add(Token(TokenType.RIGHT_PAREN, char.toString()))
                i++
            }

            char == 'p' && i + 1 < input.length && input[i + 1] == 'i' -> {
                tokens.add(Token(TokenType.PI, "π"))
                i += 2
            }

            char == 'π' -> {
                tokens.add(Token(TokenType.PI, "π"))
                i++
            }

            char == 'e' -> {
                tokens.add(Token(TokenType.E, "e"))
                i++
            }

            char == '!' -> {
                tokens.add(Token(TokenType.FACTORIAL, "!"))
                i++
            }

            char == 'l' && i + 2 < input.length && input[i + 1] == 'o' && input[i + 2] == 'g' -> {
                tokens.add(Token(TokenType.LOG, "log"))
                i += 3
            }

            char == 'l' && i + 1 < input.length && input[i + 1] == 'n' -> {
                tokens.add(Token(TokenType.LN, "ln"))
                i += 2
            }

            char == 's' && i + 2 < input.length && input[i + 1] == 'i' && input[i + 2] == 'n' -> {
                tokens.add(Token(TokenType.SIN, "sin"))
                i += 3
            }

            char == 'c' && i + 2 < input.length && input[i + 1] == 'o' && input[i + 2] == 's' -> {
                tokens.add(Token(TokenType.COS, "cos"))
                i += 3
            }

            char == 't' && i + 2 < input.length && input[i + 1] == 'a' && input[i + 2] == 'n' -> {
                tokens.add(Token(TokenType.TAN, "tan"))
                i += 3
            }

            char == 'a' && i + 3 < input.length && input[i + 1] == 's' && input[i + 2] == 'i' && input[i + 3] == 'n' -> {
                tokens.add(Token(TokenType.ASIN, "asin"))
                i += 4
            }

            char == 'a' && i + 3 < input.length && input[i + 1] == 'c' && input[i + 2] == 'o' && input[i + 3] == 's' -> {
                tokens.add(Token(TokenType.ACOS, "acos"))
                i += 4
            }

            char == 'a' && i + 3 < input.length && input[i + 1] == 't' && input[i + 2] == 'a' && input[i + 3] == 'n' -> {
                tokens.add(Token(TokenType.ATAN, "atan"))
                i += 4
            }

            char == 's' && i + 3 < input.length && input[i + 1] == 'q' && input[i + 2] == 'r' && input[i + 3] == 't' -> {
                tokens.add(Token(TokenType.SQRT, "sqrt"))
                i += 4
            }

            char == 'a' && i + 3 < input.length && input[i + 1] == 'b' && input[i + 2] == 's' && input[i + 3] == 'l' -> {
                tokens.add(Token(TokenType.ABS, "abs"))
                i += 4
            }

            char == 'r' && i + 3 < input.length && input[i + 1] == 'a' && input[i + 2] == 'n' && input[i + 3] == 'd' -> {
                tokens.add(Token(TokenType.RAND, "rand"))
                i += 4
            }

            char == '°' -> {
                tokens.add(Token(TokenType.ANGLE, "°"))
                i++
            }

            else -> {
                throw Exception("Unexpected character '$char' at position $i")
            }
        }
    }

    return tokens
}

// 推出逆波兰表达式
internal fun toRPN(tokens: List<Token>): List<Token> {
    val output = mutableListOf<Token>()
    val stack = mutableListOf<Token>()

    for (token in tokens) {
        when (token.type) {
            TokenType.NUMBER -> output.add(token)
            TokenType.PI -> output.add(token)
            TokenType.E -> output.add(token)
            TokenType.FACTORIAL -> output.add(token)
            TokenType.LOG -> stack.add(token)
            TokenType.LN -> stack.add(token)
            TokenType.SIN -> stack.add(token)
            TokenType.COS -> stack.add(token)
            TokenType.TAN -> stack.add(token)
            TokenType.ASIN -> stack.add(token)
            TokenType.ACOS -> stack.add(token)
            TokenType.ATAN -> stack.add(token)
            TokenType.SQRT -> stack.add(token)
            TokenType.ABS -> stack.add(token)
            TokenType.RAND -> stack.add(token)
            TokenType.LEFT_PAREN -> stack.add(token)
            TokenType.RIGHT_PAREN -> {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PAREN) {
                    output.add(stack.removeAt(stack.lastIndex))
                }

                if (stack.isEmpty()) {
                    throw Exception("Mismatched parentheses")
                }

                stack.removeAt(stack.lastIndex)
            }

            TokenType.PLUS -> {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PAREN) {
                    output.add(stack.removeAt(stack.lastIndex))
                }

                stack.add(token)
            }

            TokenType.MINUS -> {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PAREN) {
                    output.add(stack.removeAt(stack.lastIndex))
                }

                stack.add(token)
            }

            TokenType.MULTIPLY -> {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PAREN && stack.last().type != TokenType.PLUS && stack.last().type != TokenType.MINUS) {
                    output.add(stack.removeAt(stack.lastIndex))
                }

                stack.add(token)
            }

            TokenType.DIVIDE -> {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PAREN && stack.last().type != TokenType.PLUS && stack.last().type != TokenType.MINUS) {
                    output.add(stack.removeAt(stack.lastIndex))
                }

                stack.add(token)
            }

            TokenType.NEGATIVE -> {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PAREN && stack.last().type != TokenType.PLUS && stack.last().type != TokenType.MINUS) {
                    output.add(stack.removeAt(stack.lastIndex))
                }

                stack.add(token)
            }

            TokenType.POWER -> {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PAREN && stack.last().type != TokenType.PLUS && stack.last().type != TokenType.MINUS && stack.last().type != TokenType.MULTIPLY && stack.last().type != TokenType.DIVIDE) {
                    output.add(stack.removeAt(stack.lastIndex))
                }

                stack.add(token)
            }

            TokenType.ANGLE -> {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PAREN && stack.last().type != TokenType.PLUS && stack.last().type != TokenType.MINUS && stack.last().type != TokenType.MULTIPLY && stack.last().type != TokenType.DIVIDE && stack.last().type != TokenType.POWER) {
                    output.add(stack.removeAt(stack.lastIndex))
                }

                stack.add(token)
            }
        }
    }

    while (stack.isNotEmpty()) {
        if (stack.last().type == TokenType.LEFT_PAREN || stack.last().type == TokenType.RIGHT_PAREN) {
            throw Exception("Mismatched parentheses")
        }

        output.add(stack.removeAt(stack.lastIndex))
    }

    return output
}

// 阶乘
internal fun factorial(n: BigDecimal): BigDecimal {
    var result = BigDecimal.ONE

    for (i in 1..n.toInt()) {
        result *= BigDecimal(i)
    }

    return result
}

// 计算逆波兰表达式
internal fun evalRPN(tokens: List<Token>, scale: Int = 10): BigDecimal {
    val stack = mutableListOf<BigDecimal>()

    for (token in tokens) {
        when (token.type) {
            TokenType.NUMBER -> stack.add(token.value.toBigDecimal())
            TokenType.PI -> stack.add(Math.PI.toBigDecimal())
            TokenType.E -> stack.add(Math.E.toBigDecimal())
            TokenType.FACTORIAL -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(factorial(value))
            }

            TokenType.LOG -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(log10(value.toDouble()).toBigDecimal())
            }

            TokenType.LN -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(ln(value.toDouble()).toBigDecimal())
            }

            TokenType.SIN -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(sin(value.toDouble()).toBigDecimal())
            }

            TokenType.COS -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(cos(value.toDouble()).toBigDecimal())
            }

            TokenType.TAN -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(tan(value.toDouble()).toBigDecimal())
            }

            TokenType.ASIN -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(asin(value.toDouble()).toBigDecimal())
            }

            TokenType.ACOS -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(acos(value.toDouble()).toBigDecimal())
            }

            TokenType.ATAN -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(atan(value.toDouble()).toBigDecimal())
            }

            TokenType.ANGLE -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(Math.toRadians(value.toDouble()).toBigDecimal())
            }

            TokenType.SQRT -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(sqrt(value.toDouble()).toBigDecimal())
            }

            TokenType. ABS -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(abs(value.toDouble()).toBigDecimal())
            }

            TokenType.RAND -> {
                stack.add(Math.random().toBigDecimal())
            }

            TokenType.PLUS -> {
                val right = stack.removeAt(stack.lastIndex)
                val left = stack.removeAt(stack.lastIndex)
                stack.add(left + right)
            }

            TokenType.MINUS -> {
                val right = stack.removeAt(stack.lastIndex)
                val left = stack.removeAt(stack.lastIndex)
                stack.add(left - right)
            }

            TokenType.MULTIPLY -> {
                val right = stack.removeAt(stack.lastIndex)
                val left = stack.removeAt(stack.lastIndex)
                stack.add(left * right)
            }

            TokenType.DIVIDE -> {
                val right = stack.removeAt(stack.lastIndex)
                val left = stack.removeAt(stack.lastIndex)

                stack.add(left.divide(right, scale, RoundingMode.HALF_UP))
            }

            TokenType.POWER -> {
                val right = stack.removeAt(stack.lastIndex)
                val left = stack.removeAt(stack.lastIndex)
                stack.add(left.pow(right.toInt()))
            }

            TokenType.NEGATIVE -> {
                val value = stack.removeAt(stack.lastIndex)
                stack.add(value * BigDecimal(-1))
            }

            else -> throw Exception("Unknown token type: ${token.type}")
        }
    }
    val result = stack.removeAt(stack.lastIndex)
    // 去除小数点后面多余的0
    return result.stripTrailingZeros()
}

const val DEFAULT_DEBUG = false

/**
 * 一个简约的Kotlin计算器解析库
 *
 * Supports:
 * - 加 (+)
 * - 减 (-)
 * - 乘 (* or x)
 * - 除 (/ or ÷)
 * - 指数 (^)
 * - 括号 ()
 * - PI (pi or π)
 * - E (e)
 * - 正负号 (+ or -)
 * - 小数点 (.)
 * - 百分号 (%)
 * - 阶乘 (!)
 * - 对数 (log)
 * - 自然对数 (ln)
 * - 三角函数 (sin, cos, tan, asin, acos, atan)
 * - 反三角函数 (sin⁻¹, cos⁻¹, tan⁻¹, asin⁻¹, acos⁻¹, atan⁻¹)
 * - 平方根 (sqrt)
 * - 绝对值 (abs)
 * - 随机数 (rand)
 * - 角度转弧度 (angle)
 *
 * @param input 需要计算的字符串表达式
 * @param debug 是否显示解析过程
 * @return 计算结果
 *
 * @author RE
 */
fun eval(
    input: String,
    debug: Boolean = DEFAULT_DEBUG,
    requirement: Double? = null,
): BigDecimal {
    return if(debug) {
        val start = System.currentTimeMillis()
        println("+--------")
        println("|  Kotlin Calculator     ")
        println("|  Author: RE           ")
        println("|  Version: 1.0.0      ")
        println("|                    ")
        println("|  Input: $input")
        println("|                    ")
        val tokens = tokenize(input)
        println("|  Tokens: $tokens")
        println("|                     ")
        val rpn = toRPN(tokens)
        println("|  RPN: $rpn")
        println("|                    ")
        val result = evalRPN(rpn)
        println("|  Result: $result")
        println("|                     ")
        println("|  Time: ${System.currentTimeMillis() - start}ms")
        println("+------------------------+")
        result
    } else {
        evalRPN(toRPN(tokenize(input)))
    }.also {
        if(requirement != null) {
            if(it.toDouble() != requirement) {
                throw Exception("计算结果不符合要求, 需要: $requirement, 实际: ${it.toDouble()}")
            }
        }
    }
}