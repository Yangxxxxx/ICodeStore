package com.example.relaxword.ui.bean;

public class Word {
    private String spell;
    private int wordState;

    public Word(String spell, int wordState){
        this.spell = spell;
        this.wordState = wordState;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public int getWordState() {
        return wordState;
    }

    public void setWordState(int wordState) {
        this.wordState = wordState;
    }
}
