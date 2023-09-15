package com.nameart.maker.stylishfont.art.textonphoto.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable.ShaderFactory;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.nameart.maker.stylishfont.art.textonphoto.R;
import com.nameart.maker.stylishfont.art.textonphoto.utils.AppController;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SpHelper;
import com.nameart.maker.stylishfont.art.textonphoto.utils.SquareImageView;

public class GradientsTextAdapter extends BaseAdapter {
    private int[] colors = new int[0];
    private String gradientType;
    private String[] gradients;
    private String linearDirectiion;
    private AppController mAppController;
    private Context mContext;
    private RelativeLayout relativeLayout;
    private CardView mcvGradientsBg;
    public static ImageView imgChecked;
    private SquareImageView imgGradientsBg;
    private String[] separated;
    private TileMode tileMode = TileMode.MIRROR;
    private int clickPos;
    private boolean setClick;
    OnClickItem onClickItem;
    String type;

    public GradientsTextAdapter(Context context, String[] gradients, String gradientTile, String gradientType, String linearDirection, boolean setClick, int clickPos, OnClickItem onClickItem) {
        this.mContext = context;
        this.gradients = gradients;
        this.gradientType = gradientType;
        this.linearDirectiion = linearDirection;
        this.setClick = setClick;
        this.onClickItem = onClickItem;
        type = SpHelper.getString(mContext, SpHelper.TYPE_GRADIENT_TEXT, "");
        if (type.equals(gradientType)) {
            this.clickPos = clickPos;
        } else
            this.clickPos = -1;

    }

    public int getCount() {
        return this.gradients.length;
    }

    public Object getItem(int position) {
        return this.gradients[position];
    }

    public long getItemId(int position) {
        return this.gradients[position].length();
    }

    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view = View.inflate(this.mContext, R.layout.item_gradients, null);
        this.mcvGradientsBg = view.findViewById(R.id.mcvGradientsBg);
        this.imgGradientsBg = view.findViewById(R.id.imgGradientsBg);
        this.imgChecked = view.findViewById(R.id.imgChecked);
        ShaderFactory sf = new ShaderFactory() {
            public Shader resize(int width, int height) {
                GradientsTextAdapter.this.separated = new String[0];
                GradientsTextAdapter.this.separated = GradientsTextAdapter.this.gradients[position].split(" ");
                GradientsTextAdapter.this.colors = new int[0];
                GradientsTextAdapter.this.colors = new int[GradientsTextAdapter.this.separated.length];
                for (int i = 0; i < GradientsTextAdapter.this.separated.length; i++) {
                    GradientsTextAdapter.this.colors[i] = Color.parseColor(GradientsTextAdapter.this.separated[i]);
                }
                if (GradientsTextAdapter.this.gradientType == mContext.getResources().getString(R.string.Line)) {
                    if (GradientsTextAdapter.this.linearDirectiion == "Horizontal") {
                        return new LinearGradient(0.0f, 0.0f, (float) GradientsTextAdapter.this.imgGradientsBg.getWidth(), 0.0f, GradientsTextAdapter.this.colors, null, GradientsTextAdapter.this.tileMode);
                    } else if (GradientsTextAdapter.this.linearDirectiion != "Vertical") {
                        return null;
                    } else {
                        return new LinearGradient(0.0f, 0.0f, 0.0f, (float) GradientsTextAdapter.this.imgGradientsBg.getHeight(),
                                GradientsTextAdapter.this.colors, null, GradientsTextAdapter.this.tileMode);
                    }
                } else if (GradientsTextAdapter.this.gradientType == mContext.getResources().getString(R.string.Radial)) {
                    return new RadialGradient((float) (GradientsTextAdapter.this.imgGradientsBg.getWidth() / 2), (float) (GradientsTextAdapter.this.imgGradientsBg.getHeight() / 2), (float) GradientsTextAdapter.this.imgGradientsBg.getWidth(), GradientsTextAdapter.this.colors, null, GradientsTextAdapter.this.tileMode);
                } else {
                    if (GradientsTextAdapter.this.gradientType == mContext.getResources().getString(R.string.Sweep)) {
                        return new SweepGradient((float) (GradientsTextAdapter.this.imgGradientsBg.getWidth() / 2), (float) (GradientsTextAdapter.this.imgGradientsBg.getHeight() / 2), GradientsTextAdapter.this.colors, null);
                    }
                    return null;
                }
            }
        };
        PaintDrawable p = new PaintDrawable();
        p.setShape(new RectShape());
        p.setShaderFactory(sf);
        this.imgGradientsBg.setBackgroundDrawable(p);
        mcvGradientsBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setClick) {
                    clickPos = position;
                }
                onClickItem.onClickItem(position);
                notifyDataSetChanged();
            }
        });

        if (clickPos == position) {
            imgChecked.setVisibility(View.VISIBLE);
        } else {
            imgChecked.setVisibility(View.GONE);
        }
        return view;
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }
}
