package com.example.relaxword.ui.bean;

public class Word {
    private String spell;
    private int wordState;
    private Translation translation;

    public Word(String spell, int wordState){
        this.spell = spell;
        this.wordState = wordState;
    }

    @Override
    public String toString() {
        return spell + "\n" + (translation == null ? "null" : translation.toString());
    }

    @Override
    public boolean equals(Object obj) {
        boolean isSameWord = false;
        boolean isWordTranslation = false;
        if(obj instanceof Word){
            isSameWord = ((Word) obj).spell.equals(spell);
        }else if(obj instanceof Translation){
            isWordTranslation = ((Translation) obj).getWord().equals(spell);
        }

        return isSameWord || isWordTranslation || super.equals(obj);
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

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

}
