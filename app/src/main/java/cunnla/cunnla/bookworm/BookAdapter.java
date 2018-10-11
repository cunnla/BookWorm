package cunnla.cunnla.bookworm;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private Context mContext;
    private List <Book> bookList = new ArrayList<>();
    int[] colors = new int[2];

    public BookAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Book> list) {
        super(context, 0, list);

        mContext = context;
        bookList = list;

        colors[0] = mContext.getResources().getColor(R.color.colorPrimary);
        colors[1] = mContext.getResources().getColor(R.color.colorPrimaryLight);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_book,parent,false);

        Book currentBook = bookList.get(position);

        listItem.setBackgroundColor(colors[position % 2]);

        TextView tvDate = (TextView) listItem.findViewById(R.id.tvDate);
        //tvDate.setText(currentBook.bookDate);
        tvDate.setText(currentBook.bookDateNice());

        TextView tvName = (TextView) listItem.findViewById(R.id.tvName);
        tvName.setText(currentBook.bookName);

        TextView tvAuthor = (TextView) listItem.findViewById(R.id.tvAuthor);
        tvAuthor.setText(currentBook.bookAuthor);

        return listItem;
    }
}
