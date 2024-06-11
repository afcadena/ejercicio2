package org.calculadora;

import java.util.Scanner;

public class Main {
    public class Calculadora {

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
    
            System.out.println("Calculadora de Expresiones Matemáticas");
            String expresion = scanner.nextLine();
    
            try {
                double resultado = evaluarExpresion(expresion);
                System.out.println("Resultado: " + resultado);
            } catch (Exception e) {
                System.out.println("Error: Expresión inválida.");
            }
    
            scanner.close();
        }
    
        public static double evaluarExpresion(String expresion) {
            expresion = expresion.replaceAll("\\s+", ""); // Eliminar espacios en blanco
            return evaluar(expresion);
        }
    
        private static double evaluar(String expresion) {
            return new Object() {
                int pos = -1, actualCh;
    
                void siguienteChar() {
                    actualCh = (++pos < expresion.length()) ? expresion.charAt(pos) : -1;
                }
    
                boolean espacio() {
                    while (actualCh == ' ') siguienteChar();
                    return actualCh == -1;
                }
    
                double parsear() {
                    siguienteChar();
                    double valor = parsearExpresion();
                    if (pos < expresion.length()) throw new IllegalArgumentException("Carácter inesperado: " + (char)actualCh);
                    return valor;
                }
    
                double parsearExpresion() {
                    double valor = parsearTermino();
                    while (true) {
                        if (!espacio() && actualCh == '+') { siguienteChar(); valor += parsearTermino(); }
                        else if (!espacio() && actualCh == '-') { siguienteChar(); valor -= parsearTermino(); }
                        else return valor;
                    }
                }
    
                double parsearTermino() {
                    double valor = parsearFactor();
                    while (true) {
                        if (!espacio() && actualCh == '*') { siguienteChar(); valor *= parsearFactor(); }
                        else if (!espacio() && actualCh == '/') { siguienteChar(); valor /= parsearFactor(); }
                        else return valor;
                    }
                }
    
                double parsearFactor() {
                    double valor;
                    if (!espacio() && actualCh == '(') {
                        siguienteChar();
                        valor = parsearExpresion();
                        if (!espacio() && actualCh != ')') throw new IllegalArgumentException("Se esperaba ')'");
                        siguienteChar();
                    } else {
                        StringBuilder sb = new StringBuilder();
                        while ((actualCh >= '0' && actualCh <= '9') || actualCh == '.') { sb.append((char)actualCh); siguienteChar(); }
                        if (sb.length() == 0) throw new IllegalArgumentException("Número esperado");
                        valor = Double.parseDouble(sb.toString());
                    }
                    return valor;
                }
            }.parsear();
        }
    }
}