package org.tai.fpl.understat;

public class UnderstatNameFormatter {

    public static String formatName(String name) {
        return switch (name) {
            case "Ahmed Hegazy" -> "Ahmed Hegazi";
            case "Aleksandar Mitrovic" -> "Aleksandar Mitrović";
            case "Amad Diallo Traore" -> "Amad Diallo";
            case "Armel Bella Kotchap" -> "Armel Bella-Kotchap";
            case "Arnaut Danjuma Groeneveld" -> "Arnaut Danjuma";
            case "Asmir Begovic" -> "Asmir Begović";
            case "Benoit Badiashile Mukinayi" -> "Benoît Badiashile";
            case "Boubacar Traore" -> "Boubacar Traoré";
            case "Boubakary Soumare" -> "Boubakary Soumaré";
            case "Caglar Söyüncü" -> "Çaglar Söyüncü";
            case "Cheick Oumar Doucoure" -> "Cheick Doucouré";
            case "Clement Lenglet" -> "Clément Lenglet";
            case "Daniel N&#039;Lundulu" -> "Daniel N'Lundulu";
            case "Dara O&#039;Shea" -> "Dara O'Shea";
            case "Emile Smith-Rowe" -> "Emile Smith Rowe";
            case "Emiliano Martinez" -> "Emiliano Martínez";
            case "Ferrán Torres" -> "Ferran Torres";
            case "Franck Zambo" -> "André-Frank Zambo Anguissa";
            case "Hamed Junior Traore" -> "Hamed Traorè";
            case "Hee-Chan Hwang" -> "Hwang Hee-chan";
            case "Imran Louza" -> "Imrân Louza";
            case "Ivan Perisic" -> "Ivan Perišić";
            case "Jack O&#039;Connell" -> "Jack O'Connell";
            case "Kepa" -> "Kepa Arrizabalaga";
            case "Lewis O&#039;Brien" -> "Lewis O'Brien";
            case "Marc Guehi" -> "Marc Guéhi";
            case "Martin Odegaard" -> "Martin Ødegaard";
            case "Matias Viña" -> "Matías Viña";
            case "Matthew Cash" -> "Matty Cash";
            case "Moussa Niakhate" -> "Moussa Niakhaté";
            case "Naif Aguerd" -> "Nayef Aguerd";
            case "N&#039;Golo Kanté" -> "N'Golo Kanté";
            case "Nicolas N&#039;Koulou" -> "Nicolas Nkoulou";
            case "Nicolas Pepe" -> "Nicolas Pépé";
            case "Pape Sarr" -> "Pape Matar Sarr";
            case "Raphael Varane" -> "Raphaël Varane";
            case "Rayan Ait Nouri" -> "Rayan Aït-Nouri";
            case "Romeo Lavia" -> "Roméo Lavia";
            case "Romain Saiss" -> "Romain Saïss";
            case "Said Benrahma" -> "Saïd Benrahma";
            case "Sergi Canos" -> "Sergi Canós";
            case "Tanguy NDombele Alvaro" -> "Tanguy NDombele";
            case "Valentino Livramento" -> "Tino Livramento";
            default -> name;
        };
    }
}
