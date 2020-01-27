package mx.com.sit.pesca.fragmentos;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import mx.com.sit.pesca.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvisoResultadoFragment extends Fragment {

    PDFView pdfView;
    private File file;

    public AvisoResultadoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aviso_resultado, container, false);
        pdfView = view.findViewById(R.id.pdfView);
        Bundle data = this.getArguments();
        String path = "";
        if(data != null) {
            path = data.getString("path","");
            file = new File(path);
        }
        pdfView.fromFile(file).enableSwipe(true).swipeHorizontal(false).enableDoubletap(true).enableAntialiasing(true).load();
        return view;
    }

}
