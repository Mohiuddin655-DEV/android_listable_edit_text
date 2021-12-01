package com.bmry.listableedittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;

import com.bmry.listableedittext.databinding.LetViewLayoutBinding;

import java.util.List;

public class ListableEditText extends LinearLayout {

    private final Context context;

    private LetViewLayoutBinding binding;

    private ListableEditTextAdapter adapter;

    private PopupMenu popupMenu;

    private boolean autoValidate;
    private boolean buttonTextAllCaps;
    private boolean buttonEnable;
    private boolean enable;
    private boolean errorVisible;
    private boolean helperVisible;
    private boolean spinnerVisible;

    private ColorStateList buttonTextColor;
    private ColorStateList buttonBackgroundTint;
    private ColorStateList hintColor;
    private ColorStateList spinnerIconTint;
    private ColorStateList titleColor;

    private int buttonBackground;
    private int errorTextColor;
    private int helperTextColor;
    private int inputType;
    private int primaryColor;
    private int spinnerIcon;
    private int textColor;

    private String buttonText;
    private String errorText;
    private String helperText;
    private String hintText;
    private String text;
    private String titleText;

    private String[] entries;

    public ListableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        initView(attrs);
        listener();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.let_view_layout, this, true);
        this.binding = LetViewLayoutBinding.bind(view);
        this.adapter = new ListableEditTextAdapter();
        this.binding.recyclerView.setAdapter(adapter);
    }

    private int getDefaultPrimaryColor() {
        TypedValue primaryColorTypedValue = new TypedValue();
        try {
            context.getTheme().resolveAttribute(16843827, primaryColorTypedValue, true);
            return primaryColorTypedValue.data;
        } catch (Exception var14) {
            try {
                int colorPrimaryId = this.getResources().getIdentifier("colorPrimary", "attr", this.getContext().getPackageName());
                if (colorPrimaryId == 0) {
                    throw new RuntimeException("colorPrimary not found");
                }
                context.getTheme().resolveAttribute(colorPrimaryId, primaryColorTypedValue, true);
                return primaryColorTypedValue.data;
            } catch (Exception var13) {
                return this.getResources().getColor(android.R.color.holo_blue_dark);
            }
        }
    }

    private void initView(@Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListableEditText);

        int count = typedArray.getIndexCount();

        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                initView(typedArray, attr);
            }
        } finally {
            typedArray.recycle();
        }
    }

    @SuppressLint({"ResourceAsColor", "NonConstantResourceId"})
    private void initView(@NonNull TypedArray typedArray, @StyleableRes int attr) {
        if (attr == R.styleable.ListableEditText_let_autoValidate) {
            setAutoValidate(typedArray.getBoolean(attr, false));
        } else if (attr == R.styleable.ListableEditText_let_buttonBackground) {
            setButtonBackground(typedArray.getResourceId(attr, R.drawable.let_button_background));
        } else if (attr == R.styleable.ListableEditText_let_buttonBackgroundTint) {
            setButtonBackgroundTint(typedArray.getColorStateList(attr));
        } else if (attr == R.styleable.ListableEditText_let_buttonEnable) {
            setButtonEnable(typedArray.getBoolean(attr, true));
        } else if (attr == R.styleable.ListableEditText_let_buttonText) {
            setButtonText(typedArray.getString(attr));
        } else if (attr == R.styleable.ListableEditText_let_buttonTextAllCaps) {
            setButtonTextAllCaps(typedArray.getBoolean(attr, true));
        } else if (attr == R.styleable.ListableEditText_let_buttonTextColor) {
            setButtonTextColor(typedArray.getColorStateList(attr));
        } else if (attr == R.styleable.ListableEditText_let_enable) {
            setEnable(typedArray.getBoolean(attr, true));
        } else if (attr == R.styleable.ListableEditText_let_entries) {
            setEntries(typedArray.getResourceId(attr, -1));
        } else if (attr == R.styleable.ListableEditText_let_errorText) {
            setErrorText(typedArray.getString(attr));
        } else if (attr == R.styleable.ListableEditText_let_errorTextColor) {
            setErrorTextColor(typedArray.getColor(attr, this.getResources().getColor(R.color.lef_view_default_error_color)));
        } else if (attr == R.styleable.ListableEditText_let_errorVisible) {
            setErrorVisible(typedArray.getBoolean(attr, false));
        } else if (attr == R.styleable.ListableEditText_let_helperText) {
            setHelperText(typedArray.getString(attr));
        } else if (attr == R.styleable.ListableEditText_let_helperTextColor) {
            setHelperTextColor(typedArray.getColor(attr, this.getResources().getColor(R.color.lef_view_default_exception_color)));
        } else if (attr == R.styleable.ListableEditText_let_helperVisible) {
            setHelperVisible(typedArray.getBoolean(attr, false));
        } else if (attr == R.styleable.ListableEditText_let_hintColor) {
            setHintColor(typedArray.getColorStateList(attr));
        } else if (attr == R.styleable.ListableEditText_let_hintText) {
            setHintText(typedArray.getString(attr));
        } else if (attr == R.styleable.ListableEditText_let_inputType) {
            setInputType(typedArray.getInt(attr, 0));
        } else if (attr == R.styleable.ListableEditText_let_primaryColor) {
            setPrimaryColor(typedArray.getColor(attr, getDefaultPrimaryColor()));
        } else if (attr == R.styleable.ListableEditText_let_spinnerIcon) {
            setSpinnerIcon(typedArray.getResourceId(attr, R.drawable.let_icon_drop_down));
        } else if (attr == R.styleable.ListableEditText_let_spinnerTint) {
            setSpinnerIconTint(typedArray.getColorStateList(attr));
        } else if (attr == R.styleable.ListableEditText_let_spinnerVisible) {
            setSpinnerVisible(typedArray.getBoolean(attr, false));
        } else if (attr == R.styleable.ListableEditText_let_text) {
            setText(typedArray.getString(attr));
        } else if (attr == R.styleable.ListableEditText_let_textColor) {
            setTextColor(typedArray.getColor(attr, this.getResources().getColor(R.color.let_list_item_default_text_color)));
        } else if (attr == R.styleable.ListableEditText_let_titleText) {
            setTitleText(typedArray.getString(attr));
        } else if (attr == R.styleable.ListableEditText_let_titleTextColor) {
            setTitleColor(typedArray.getColorStateList(attr));
        }
    }

    public ColorStateList getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(ColorStateList color) {
        this.titleColor = color;
        this.binding.tvTitle.setTextColor(color);
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(@ColorInt int color) {
        this.primaryColor = color;
        this.binding.etBody.setPrimaryColor(primaryColor);
        this.binding.actionAdd.getBackground().setTint(primaryColor);
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }

    public void setErrorTextColor(@ColorInt int color) {
        this.errorTextColor = color;
        this.binding.tvErrorText.setTextColor(errorTextColor);
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String text) {
        this.errorText = text;
        this.binding.tvErrorText.setText(errorText);
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String title) {
        this.titleText = title;
        this.binding.tvTitle.setText(titleText);
        if (!TextUtils.isEmpty(title)) {
            this.binding.tvTitle.setVisibility(VISIBLE);
        } else {
            this.binding.tvTitle.setVisibility(GONE);
        }
    }

    public boolean isAutoValidate() {
        return autoValidate;
    }

    public void setAutoValidate(boolean enable) {
        this.autoValidate = enable;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int color) {
        this.textColor = color;
        this.binding.etBody.setTextColor(textColor);
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hint) {
        this.hintText = hint;
        this.binding.etBody.setHint(hintText);
    }

    public ColorStateList getHintColor() {
        return hintColor;
    }

    public void setHintColor(ColorStateList color) {
        this.hintColor = color;
        this.binding.etBody.setMetHintTextColor(hintColor);
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String text) {
        this.buttonText = text;
        this.binding.actionAdd.setText(buttonText);
    }

    public boolean isButtonTextAllCaps() {
        return buttonTextAllCaps;
    }

    public void setButtonTextAllCaps(boolean allCaps) {
        this.buttonTextAllCaps = allCaps;
        this.binding.actionAdd.setAllCaps(buttonTextAllCaps);
    }

    public ColorStateList getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(ColorStateList color) {
        this.buttonTextColor = color;
        this.binding.actionAdd.setTextColor(buttonTextColor);
    }

    public int getButtonBackground() {
        return buttonBackground;
    }

    public void setButtonBackground(@DrawableRes int background) {
        this.buttonBackground = background;
        this.binding.actionAdd.setBackgroundResource(buttonBackground);
    }

    public ColorStateList getButtonBackgroundTint() {
        return buttonBackgroundTint;
    }

    public void setButtonBackgroundTint(@NonNull ColorStateList color) {
        this.buttonBackgroundTint = color;
        this.binding.actionAdd.getBackground().setTintList(buttonBackgroundTint);
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        super.setEnabled(enable);
        this.enable = enable;
        this.binding.tvTitle.setEnabled(this.enable);
        this.binding.actionAdd.setEnabled(this.enable);
        this.binding.actionSpinner.setEnabled(this.enable);
        this.binding.etBody.setEnabled(this.enable);
    }

    public String[] getEntries() {
        return entries;
    }

    public void setEntries(@ArrayRes int resId) {
        this.entries = context.getResources().getStringArray(resId);
        this.setSpinnerVisible(true);
        this.createSpinnerOptions(entries);
    }

    public List<String> getSelectedItems() {
        if (adapter != null) {
            return adapter.getItems();
        } else {
            return null;
        }
    }

    public String getHelperText() {
        return helperText;
    }

    public void setHelperText(String text) {
        this.helperText = text;
        this.binding.tvHelperText.setText(helperText);
    }

    public boolean isSpinnerVisible() {
        return spinnerVisible;
    }

    public void setSpinnerVisible(boolean visible) {
        if (visible && entries != null) {
            this.spinnerVisible = false;
            binding.actionSpinner.setVisibility(VISIBLE);
        } else {
            this.spinnerVisible = false;
            binding.actionSpinner.setVisibility(GONE);
        }
    }

    public boolean isButtonEnable() {
        return buttonEnable;
    }

    public void setButtonEnable(boolean enable) {
        this.buttonEnable = enable;
        this.binding.actionAdd.setEnabled(buttonEnable);
    }

    public int getHelperTextColor() {
        return helperTextColor;
    }

    public void setHelperTextColor(@ColorInt int color) {
        this.helperTextColor = color;
        this.binding.tvHelperText.setTextColor(helperTextColor);
    }

    public int getSpinnerIcon() {
        return spinnerIcon;
    }

    public void setSpinnerIcon(@DrawableRes int icon) {
        this.spinnerIcon = icon;
        this.binding.actionSpinner.setImageResource(spinnerIcon);
    }

    public ColorStateList getSpinnerIconTint() {
        return spinnerIconTint;
    }

    public void setSpinnerIconTint(ColorStateList tint) {
        this.spinnerIconTint = tint;
        this.binding.actionSpinner.setImageTintList(tint);
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        switch (inputType) {
            case 1:
                this.inputType = EditorInfo.TYPE_CLASS_NUMBER;
                break;
            case 2:
                this.inputType = EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS;
                break;
            default:
                this.inputType = EditorInfo.TYPE_CLASS_TEXT;
                break;
        }
        this.binding.etBody.setInputType(this.inputType);
    }

    public String getText() {
        return text;
    }

    public void setText(String defaultText) {
        this.text = defaultText;
        this.binding.etBody.setText(text);
    }

    public boolean isErrorVisible() {
        return errorVisible;
    }

    public void setErrorVisible(boolean visible) {
        this.errorVisible = visible;
        if (visible) {
            this.binding.tvErrorText.setVisibility(VISIBLE);
        } else {
            this.binding.tvErrorText.setVisibility(GONE);
        }
    }

    public boolean isHelperVisible() {
        return helperVisible;
    }

    public void setHelperVisible(boolean visible) {
        this.helperVisible = visible;
        if (visible) {
            this.binding.tvHelperText.setVisibility(VISIBLE);
        } else {
            this.binding.tvHelperText.setVisibility(GONE);
        }
    }

    public void createSpinnerOptions(String[] entries) {
        this.popupMenu = new PopupMenu(context, binding.etBody);
        if (entries != null && entries.length > 0) {
            for (String spinnerItem : entries) {
                popupMenu.getMenu().add(spinnerItem);
            }
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            this.binding.etBody.setText(item.getTitle());
            return false;
        });
    }

    private void listener() {
        binding.actionAdd.setOnClickListener(v -> {
            String value = String.valueOf(binding.etBody.getText()).trim();
            if (TextUtils.isEmpty(value)) {
                setErrorVisible(true);
            } else if (adapter.getItems().contains(getCaseSensitive(value))) {
                setHelperVisible(true);
            } else {
                adapter.addItem(getCaseSensitive(value));
                binding.etBody.setText("");
            }
        });

        binding.actionSpinner.setOnClickListener(v ->
                showSpinner());

        adapter.setListener((data, position, longClick) -> {
            adapter.removeItem(position);
            alreadyAddedVisible(data.toString());
        });

        binding.etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = String.valueOf(s).trim();
                alreadyAddedVisible(value);
                if (autoValidate) {
                    setButtonEnable(!TextUtils.isEmpty(value) && !adapter.getItems().contains(value));
                }
            }
        });
    }

    private String getCaseSensitive(String value) {
        if (inputType == EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS) {
            return value.toUpperCase();
        } else {
            return value;
        }
    }

    private void alreadyAddedVisible(String value) {
        binding.tvErrorText.setVisibility(GONE);
        if (adapter.getItems().contains(getCaseSensitive(value))) {
            binding.tvHelperText.setVisibility(VISIBLE);
        } else {
            binding.tvHelperText.setVisibility(GONE);
        }
    }

    public void showSpinner() {
        if (popupMenu != null) {
            popupMenu.show();
        } else {
            popupMenu = new PopupMenu(context, binding.etBody);
        }
    }

}
