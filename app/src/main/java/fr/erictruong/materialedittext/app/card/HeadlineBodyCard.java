package fr.erictruong.materialedittext.app.card;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Space;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.erictruong.materialedittext.app.R;

public class HeadlineBodyCard extends Card {

    protected CharSequence headline, body1;

    public HeadlineBodyCard(int id, CharSequence headline, CharSequence body1) {
        this(id, TYPE_HEADLINE_BODY_1, headline, body1);
    }

    public HeadlineBodyCard(int id, int viewType, CharSequence headline, CharSequence body1) {
        super(id, viewType);
        this.headline = headline;
        this.body1 = body1;
    }

    public CharSequence getHeadline() {
        return headline;
    }

    public CharSequence getBody1() {
        return body1;
    }

    /**
     * Provide a reference to the views for each data item. Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements CardHolder {

        @InjectView(R.id.tv_headline) TextView tvHeadline;
        @InjectView(R.id.tv_body) TextView tvBody1;
        @InjectView(R.id.space_headline_body) View spaceHeadlineBody1;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }

        @Override
        public void bindView(Card card, int position) {
            HeadlineBodyCard c = (HeadlineBodyCard) card;
            bindView(c.getHeadline(), c.getBody1());
        }

        protected void bindView(CharSequence headline, CharSequence body1) {
            // Hide headline view if text empty
            if (!TextUtils.isEmpty(headline)) {
                tvHeadline.setVisibility(View.VISIBLE);
                tvHeadline.setText(headline);
            } else {
                tvHeadline.setVisibility(View.GONE);
            }
            // Hide body view if text empty
            if (!TextUtils.isEmpty(body1)) {
                tvBody1.setVisibility(View.VISIBLE);
                tvBody1.setText(body1);
            } else {
                tvBody1.setVisibility(View.GONE);
            }

            if (tvHeadline.getVisibility() == View.VISIBLE && tvBody1.getVisibility() == View.VISIBLE) {
                spaceHeadlineBody1.setVisibility(View.VISIBLE);
            } else {
                spaceHeadlineBody1.setVisibility(View.GONE);
            }
        }
    }
}

