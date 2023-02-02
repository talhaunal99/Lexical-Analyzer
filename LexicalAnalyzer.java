import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class LexicalAnalyzer {
    protected ArrayList <String> lexemeList;
    protected File inputFile;
    protected File outputFile;
    private static boolean isError = false;
    //TODO input, output file must be read from console

    protected void readFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            int c = 0;
            while ((c = br.read()) != -1) {
                char character = (char) c;
                lexemeList.add("" + character);
            }
            br.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String lexeme) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(outputFile, true));
            out.write(lexeme);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String printStatement(String lexeme) {
        String token = "";
        if(lexeme.equals("(")) {
            token = "LPARANT            ";
        } else if(lexeme.equals(")")) {
            token = "RPARANT            ";
        } else if(lexeme.equals("=")) {
            token = "ASSIGNM            ";
        } else if(lexeme.equals(";")) {
            token = "SEMICOLON          ";
        } else if(lexeme.equals(">")) {
            token = "GREATER            ";
        } else if(lexeme.equals("<")) {
            token = "LESS               ";
        } else if(lexeme.equals("{")) {
            token = "LCURLYB            ";
        } else if(lexeme.equals("}")) {
            token = "RCURLYB            ";
        }else if(lexeme.equals("-")) {
            token = "SUBT               ";
        }else if(lexeme.equals("/")) {
            token = "DIV                ";
        } else if(lexeme.equals("*")) {
            token = "MULT               ";
        } else if(lexeme.equals("+")) {
            token = "ADD                ";
        } else if(lexeme.length() == 1 && Character.isLetter(lexeme.charAt(0)) ){
            token = "IDENTIFIER         ";
        }else if(lexeme.equals(">=")) {
            token = "GRE_EQ             ";
        }else if(lexeme.equals("<=")) {
            token = "LESS_EQ            ";
        } else if(lexeme.equals("for")) {
            token = "FOR_STATEMENT      ";
        } else if(lexeme.equals("int")) {
            token = "INT_TYPE           ";
        } else if(lexeme.equals("char")) {
            token = "CHAR_TYPE          ";
        } else if(lexeme.equals("return")) {
            token = "RETURN_STMT        ";
        } else if(isParsable(lexeme)) {
            token = "INT_LIT            ";
        } else if(lexeme.equals("!") || lexeme.equals("@") || lexeme.equals("#") || lexeme.equals("$") || lexeme.equals("%")) {
            token = "UNKNOWN_OPERATOR   ";
            isError = true;
        } else {
            token = "UNKNOWN_IDENTIFIER ";
            isError = true;
        }

        return "Next token is " + token + "Next lexeme is " + lexeme + '\n';
    }
    private void printList() {
        for (String lexem : lexemeList)
            System.out.println(lexem);
    }

    protected void  traverseList() {
        for(int i = 0; i < lexemeList.size() ; i++) {
            if(lexemeList.get(i).equals("(") || lexemeList.get(i).equals(")") || lexemeList.get(i).equals("=")
                    || lexemeList.get(i).equals(";") || lexemeList.get(i).equals("{") || lexemeList.get(i).equals("}")
                    || lexemeList.get(i).equals("-") || lexemeList.get(i).equals("/") || lexemeList.get(i).equals("*")
                    || lexemeList.get(i).equals("+")) {
                writeFile(printStatement(lexemeList.get(i)));
            } else if(lexemeList.get(i).equals(">") || lexemeList.get(i).equals("<")) {
                if(lexemeList.get(i+1) != null && lexemeList.get(i+1).equals("=")) {
                    writeFile(printStatement(lexemeList.get(i) + lexemeList.get(i+1)));
                    i++;
                } else {
                    writeFile(printStatement(lexemeList.get(i)));
                }
            } else if(Character.isLetter(lexemeList.get(i).charAt(0))) {
                if(lexemeList.get(i).equals("c") && equalNullCheck(lexemeList.get(i+1), "h") &&
                        equalNullCheck(lexemeList.get(i+2), "a") && equalNullCheck(lexemeList.get(i+3), "r")
                        && equalNullCheck(lexemeList.get(i+4), " ")) { //char
                    writeFile(printStatement("char"));
                    i += 5;
                    if((lexemeList.get(i) != null && Character.isLetter(lexemeList.get(i).charAt(0))) && (equalNullCheck(lexemeList.get(i+1), ";"))) {
                        writeFile(printStatement(lexemeList.get(i)));
//                        i++;
                    } else if((lexemeList.get(i) != null && Character.isLetter(lexemeList.get(i).charAt(0))) &&
                            equalNullCheck(lexemeList.get(i+1), " ") && equalNullCheck(lexemeList.get(i+2), "=")) {
                        writeFile(printStatement(lexemeList.get(i)));
//                        i++;
                    } else {
                        String multipleCharacterVar = "";
                        multipleCharacterVar += lexemeList.get(i);
                        i++;
                        while((lexemeList.get(i) != null) && !lexemeList.get(i).equals(" ") && !lexemeList.get(i).equals("=") &&
                                !lexemeList.get(i).equals(";") && !isOperator(lexemeList.get(i))) {
                            multipleCharacterVar += lexemeList.get(i);
                            i++;
                        }
                        i--;
                        writeFile(printStatement(multipleCharacterVar));
                    }
                } else if(lexemeList.get(i).equals("i") && equalNullCheck(lexemeList.get(i+1), "n") &&
                        equalNullCheck(lexemeList.get(i+2), "t") && equalNullCheck(lexemeList.get(i+3), " ")) { //int
                    writeFile(printStatement("int"));
                    i += 4;
                    if((lexemeList.get(i) != null && Character.isLetter(lexemeList.get(i).charAt(0))) &&
                            (equalNullCheck(lexemeList.get(i+1), ";"))) {
                        writeFile(printStatement(lexemeList.get(i)));
//                        i++;
                    } else if((lexemeList.get(i) != null && Character.isLetter(lexemeList.get(i).charAt(0))) &&
                            equalNullCheck(lexemeList.get(i+1), " ") && equalNullCheck(lexemeList.get(i+2), "=")) {
                        writeFile(printStatement(lexemeList.get(i)));
//                        i++;
                    }  else {
                        String multipleCharacterVar = "";
                        multipleCharacterVar += lexemeList.get(i);
                        i++;
                        while((lexemeList.get(i) != null) && !lexemeList.get(i).equals(" ") && !lexemeList.get(i).equals("=") &&
                                !lexemeList.get(i).equals(";") && !isOperator(lexemeList.get(i))) {
                            multipleCharacterVar += lexemeList.get(i);
                            i++;
                        }
                        i--;
                        writeFile(printStatement(multipleCharacterVar));
                    }
                } else if(lexemeList.get(i).equals("f") && equalNullCheck(lexemeList.get(i+1), "o") && equalNullCheck(lexemeList.get(i+2), "r")) {
                    writeFile(printStatement("for"));
                    i += 2;
                }  else if(lexemeList.get(i).equals("r") && equalNullCheck(lexemeList.get(i+1), "e") && equalNullCheck(lexemeList.get(i+2), "t") &&
                        equalNullCheck(lexemeList.get(i+3), "u") && equalNullCheck(lexemeList.get(i+4), "r") && equalNullCheck(lexemeList.get(i+5), "n")) {
                    writeFile(printStatement("return"));
                    i += 5;
                } else {
                    String varName = lexemeList.get(i);
                    i++;
                    while((lexemeList.get(i) != null) && !lexemeList.get(i).equals(" ") && !lexemeList.get(i).equals("=") &&
                            !lexemeList.get(i).equals(";") && !isOperator(lexemeList.get(i))) {
                        varName += lexemeList.get(i);
                        i++;
                    }
                    i--;
                    writeFile(printStatement(varName));
                }

            } else if(Character.isDigit(lexemeList.get(i).charAt(0))) { // integer number
                String number = lexemeList.get(i);
                i++;
                while((lexemeList.get(i) != null) && (Character.isDigit(lexemeList.get(i).charAt(0))) ) {
                    number += lexemeList.get(i);
                    i++;
                }
                i--;
                writeFile(printStatement(number));

            } else if(lexemeList.get(i) != null && !lexemeList.get(i).equals(" ") && !lexemeList.get(i).equals(System.lineSeparator()) ){
                writeFile(printStatement(lexemeList.get(i)));
            }

            if(isError == true) System.exit(0);
        }
    }

    private boolean equalNullCheck(String s1, String s2) {
        if(s1 != null && s2 != null) {
            return s1.equals(s2);
        }
        return false;
    }

    private boolean isOperator(String s) {
        if(s != null && (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") ))
            return true;
        return false;
    }
    private boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
//    public static void main(String[] args) {
//        LexicalAnalyzer analyzer = new LexicalAnalyzer();
//        analyzer.inputFile = new File(args[1]);
//        analyzer.outputFile = new File(args[2);
//        analyzer.lexemeList = new ArrayList<>();
//        analyzer.readFile();
//        analyzer.traverseList();
//    }
}
    