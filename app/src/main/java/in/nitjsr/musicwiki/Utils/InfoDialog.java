package in.nitjsr.musicwiki.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import in.nitjsr.musicwiki.R;

public class InfoDialog extends DialogFragment {
    String data, title;

    public InfoDialog(@NonNull String title, String data) {
        this.data = data;
        this.title = title;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d("hello",data);
        Context context = getActivity();
        View view = LayoutInflater.from(context).inflate(R.layout.info_dialog, null);
        TextView content,name;
        content = view.findViewById(R.id.content);
        name = view.findViewById(R.id.title);

        content.setText(Html.fromHtml(data));
        name.setText(title);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .create();

        return builder.create();
    }
}
