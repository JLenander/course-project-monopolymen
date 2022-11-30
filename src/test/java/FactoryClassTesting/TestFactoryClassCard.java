package FactoryClassTesting;
import static org.junit.Assert.*;

import GameEntities.Board;
import GameEntities.Cards.*;
import GameEntities.FactoryBoard;
import GameEntities.Tiles.*;


import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestFactoryClassCard {
    @Test
    public void TestOutOfJailFreeCard() throws FileNotFoundException {
        //test the OutOfJailFreeCard method
        List<List<String>> cardLines = FactoryProperty.extractor("src/main/resources/Cards.csv");
        cardLines.remove(0);
        GetOutOfJailCard test = FactoryCard.OutOfJailFreeCard(cardLines.get(0));
        assertTrue(test.isChanceCard());
        assertEquals(test.getCardDescription(),
                "Get Out Of Jail Free.This card may be kept until needed or traded");
        assertEquals(test.getCardName(), "card_chance_1");
    }
    @Test
    public void TestRepairCard() throws FileNotFoundException {
        //test the RepairCard method
        List<List<String>> cardLines = FactoryProperty.extractor("src/main/resources/Cards.csv");
        cardLines.remove(0);
        PropertyRepairCard test = FactoryCard.repairCard(cardLines.get(1));
        assertTrue(test.isChanceCard());
        assertEquals(test.getCardName(), "card_chance_2");
        assertEquals(test.getCardDescription(),
                "Make general repairs on all your property: For each House pay $25 and For each Hotel pay $100");
    }
    @Test
    public void testMoneyCard() throws FileNotFoundException {
        //test the MoneyCard method
        List<List<String>> cardLines = FactoryProperty.extractor("src/main/resources/Cards.csv");
        cardLines.remove(0);
        MoneyCard test = FactoryCard.moneyCard(cardLines.get(2));
        assertTrue(test.isChanceCard());
        assertEquals(test.getCardName(), "card_chance_3");
        assertEquals(test.getCardDescription(),
                "Bank pays your dividend of $50");
    }
    @Test
    public void testMoveBackCard() throws FileNotFoundException {
        //test the MoveBackCard
        List<List<String>> cardLines = FactoryProperty.extractor("src/main/resources/Cards.csv");
        cardLines.remove(0);
        AdvanceCard test = FactoryCard.moveBackCard(cardLines.get(11));
        assertTrue(test.isChanceCard());
        assertEquals(test.getCardName(), "card_chance_12");
        assertEquals(test.getCardDescription(),
                "Go back three spaces");
    }
    @Test
    public void testCommunityCard() throws FileNotFoundException {
        List<List<String>> cardLines = FactoryProperty.extractor("src/main/resources/Cards.csv");
        cardLines.remove(0);
        MoneyCard test = FactoryCard.moneyCard(cardLines.get(31));
        assertFalse(test.isChanceCard());
    }
    @Test
    public void testAdvanceCard() throws FileNotFoundException {
        Board temp = FactoryBoard.boardMaker
                ("src/main/resources/Data/property_csvs/Color Properties Monopoly.csv",
                        "src/main/resources/Data/property_csvs/Utility Properties Monopoly.csv",
                        "src/main/resources/Data/property_csvs/Station Properties Monopoly.csv",
                        "src/main/resources/Cards.csv");
        List<List<String>> cardLines = FactoryProperty.extractor("src/main/resources/Cards.csv");
        cardLines.remove(0);
        AdvanceCard test = FactoryCard.advanceCard(cardLines.get(4), temp);
        assertTrue(test.isChanceCard());
    }
    @Test
    public void testAdvanceNearCard() throws FileNotFoundException {
        Board temp = FactoryBoard.boardMaker
                ("src/main/resources/Data/property_csvs/Color Properties Monopoly.csv",
                        "src/main/resources/Data/property_csvs/Utility Properties Monopoly.csv",
                        "src/main/resources/Data/property_csvs/Station Properties Monopoly.csv",
                        "src/main/resources/Cards.csv");
        List<List<String>> cardLines = FactoryProperty.extractor("src/main/resources/Cards.csv");
        cardLines.remove(0);
        GoToNearestCard test = FactoryCard.advanceNearCard(cardLines.get(6), temp);
        assertTrue(test.isChanceCard());
        assertEquals(test.getCardName(), "card_chance_7");
    }
    @Test
    public void testGetCards() throws FileNotFoundException {
        Board temp = FactoryBoard.boardMaker
                ("src/main/resources/Data/property_csvs/Color Properties Monopoly.csv",
                        "src/main/resources/Data/property_csvs/Utility Properties Monopoly.csv",
                        "src/main/resources/Data/property_csvs/Station Properties Monopoly.csv",
                        "src/main/resources/Cards.csv");
        ArrayList<Card>[] list = FactoryCard.getCards("src/main/resources/Cards.csv", temp);
        int chanceNum = 0;
        int communityNum = 0;
        for (Card chance : list[0]){
            assertTrue(chance.isChanceCard());
            chanceNum++;
        }
        for (Card chance : list[1]){
            assertFalse(chance.isChanceCard());
            communityNum++;
        }
        assertEquals(16, chanceNum);
        assertEquals(16, communityNum);
    }
}
