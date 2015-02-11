package fr.erictruong.materialedittext.app.card;

public abstract class Card {

    public static final int TYPE_HEADLINE_BODY_1 = 1;

    protected int id;
    protected int viewType;

    public Card(int id, int viewType) {
        this.id = id;
        this.viewType = viewType;
    }

    public int getId() {
        return id;
    }

    public int getViewType() {
        return viewType;
    }

    public interface CardHolder {

        public void bindView(Card card, int position);
    }
}