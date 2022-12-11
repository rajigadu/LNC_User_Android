package com.latenightchauffeurs.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.activity.Rating;
import com.latenightchauffeurs.model.ItemModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleSelectionAdapter extends RecyclerView.Adapter
{
    private List<ItemModel> itemModels;
    private Context context;
    public int lastCheckedPosition = -1;

    public SingleSelectionAdapter(Context context, List<ItemModel> itemModels)
    {
        this.itemModels = itemModels;
        this.context = context;
    }

    @Override
    public int getItemCount()
    {
        return itemModels.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_single, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ItemModel model = (ItemModel) itemModels.get(position);
        initializeViews(model, holder, position);
    }

    private void initializeViews(final ItemModel model, final RecyclerView.ViewHolder holder, int position)
    {
         String value=getColoredSpanned(model.getName()+"%", "#800000");

        ((ItemViewHolder)holder).name.setText(Html.fromHtml(value));
        if (model.getId() == lastCheckedPosition)
        {
            ((ItemViewHolder)holder).radioButton.setChecked(true);
        }
        else
        {
            ((ItemViewHolder)holder).radioButton.setChecked(false);
        }
        ((ItemViewHolder)holder).radioButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((SingleSelectionAdapter.ItemViewHolder)holder).radioButton.setChecked(true);
                lastCheckedPosition = model.getId();
                notifyItemRangeChanged(0, itemModels.size());

                ((Rating)context).ddd();
            }
        });
    }

    public ItemModel getSelectedItem()
    {
        ItemModel model = (ItemModel) itemModels.get(lastCheckedPosition);

        if (model !=null)
        {
            return model;
        }
        return null;
    }
    public int selectedPosition()
    {
        return lastCheckedPosition;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.radio)
        RadioButton radioButton;

        public ItemViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private static String getColoredSpanned(String text, String color)
    {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}