package DTO;

public enum Store {
    STEAM("1", "Steam"),
    GAMERSGATE("2", "GamersGate"),
    GREEN_MAN_GAMING("3", "GreenManGaming"),
    AMAZON("4", "Amazon"),
    GAMESTOP("5", "GameStop"),
    DIRECT2DRIVE("6", "Direct2Drive"),
    GOG("7", "GOG"),
    ORIGIN("8", "Origin"),
    GET_GAMES("9", "Get Games"),
    SHINY_LOOT("10", "Shiny Loot"),
    HUMBLE_STORE("11", "Humble Store"),
    DESURA("12", "Desura"),
    UPLAY("13", "Uplay"),
    INDIEGAMESTAND("14", "IndieGameStand"),
    FANATICAL("15", "Fanatical"),
    GAMESROCKET("16", "Gamesrocket"),
    GAMES_REPUBLIC("17", "Games Republic"),
    SILAGAMES("18", "SilaGames"),
    PLAYFIELD("19", "Playfield"),
    IMPERIALGAMES("20", "ImperialGames"),
    WINGAMESTORE("21", "WinGameStore"),
    FUNSTOCKDIGITAL("22", "FunStockDigital"),
    GAMEBILLET("23", "GameBillet"),
    VOIDU("24", "Voidu"),
    EPIC_GAMES_STORE("25", "Epic Games Store"),
    RAZER_GAME_STORE("26", "Razer Game Store"),
    GAMESPLANET("27", "Gamesplanet"),
    GAMESLOAD("28", "Gamesload"),
    TWOGAME("29", "2Game"),
    INDIEGALA("30", "IndieGala"),
    BLIZZARD_SHOP("31", "Blizzard Shop"),
    ALLYOUPLAY("32", "AllYouPlay"),
    DLGAMER("33", "DLGamer"),
    NOCTRE("34", "Noctre"),
    DREAMGAME("35", "DreamGame");

    private final String storeID;
    private final String storeName;

    Store(String storeID, String storeName) {
        this.storeID = storeID;
        this.storeName = storeName;
    }

    public String getStoreID() {
        return storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public static String getStoreNameById(String id) {
        for (Store store : Store.values()) {
            if (store.getStoreID().equals(id)) {
                return store.getStoreName();
            }
        }
        return null; // or throw an exception if preferred
    }
    }
