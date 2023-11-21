package com.daniel.plusnote.conspected_code;//package com.example.plusnote;
//
//import android.annotation.SuppressLint;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.widget.ViewPager2;
//
//import java.time.LocalDate;
//import java.time.Month;
//import java.time.format.DateTimeFormatter;
//import java.util.Locale;
//
//public class PagerFragment extends Fragment {
//
//    private static int pageNumber1;
//    int year = 2022;
//    public static int pagenum = 5;
//
//    public static PagerFragment newInstance(int page) {
//        PagerFragment fragment = new PagerFragment();
//        Bundle args = new Bundle();
//        args.putInt("num", page);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public PagerFragment() {
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        pageNumber1 = getArguments() != null ? getArguments().getInt("num") : 1;
//
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View result = inflater.inflate(R.layout.fragment_page, container, false);
//        View main = inflater.inflate(R.layout.activity_main, container, false);
//        TextView textV1 = result.findViewById(R.id.month1);
//        TextView textV2 = result.findViewById(R.id.month2);
//        TextView textV3 = result.findViewById(R.id.month3);
//        TextView textV4 = result.findViewById(R.id.month4);
//        TextView textV5 = result.findViewById(R.id.month5);
//        TextView textV6 = result.findViewById(R.id.month6);
//        TextView yearV = result.findViewById(R.id.Year);
//        ConstraintLayout c = result.findViewById(R.id.container);
//        ConstraintLayout c1 = result.findViewById(R.id.container1);
//        ConstraintLayout c2 = result.findViewById(R.id.container2);
//        ConstraintLayout c3 = result.findViewById(R.id.container3);
//        ConstraintLayout c4 = result.findViewById(R.id.container4);
//        ConstraintLayout c5 = result.findViewById(R.id.container5);
//        ConstraintLayout c6 = result.findViewById(R.id.container6);
//        ViewPager2 vp2 = main.findViewById(R.id.vp2);
//        c1.setOnClickListener(view -> {
//            textV1.setTextColor(Color.parseColor("#FFA500"));
//            textV6.setTextColor(Color.parseColor("#9000FF00"));
//            textV2.setTextColor(Color.parseColor("#9000FF00"));
//            textV3.setTextColor(Color.parseColor("#9000FF00"));
//            textV4.setTextColor(Color.parseColor("#9000FF00"));
//            textV5.setTextColor(Color.parseColor("#9000FF00"));
//        });
//        c2.setOnClickListener(view -> {
//            textV2.setTextColor(Color.parseColor("#FFA500"));
//            textV6.setTextColor(Color.parseColor("#9000FF00"));
//            textV1.setTextColor(Color.parseColor("#9000FF00"));
//            textV3.setTextColor(Color.parseColor("#9000FF00"));
//            textV4.setTextColor(Color.parseColor("#9000FF00"));
//            textV5.setTextColor(Color.parseColor("#9000FF00"));
//            if (pageNumber1%2==0){
//                LocalDate ld = LocalDate.of(2022, Month.FEBRUARY, 1);
//                pagenum = (ld.getDayOfYear()+5)/7;
//                vp2.setCurrentItem(pagenum);
//            }
//        });
//        c3.setOnClickListener(view -> {
//            textV3.setTextColor(Color.parseColor("#FFA500"));
//            textV6.setTextColor(Color.parseColor("#9000FF00"));
//            textV1.setTextColor(Color.parseColor("#9000FF00"));
//            textV2.setTextColor(Color.parseColor("#9000FF00"));
//            textV4.setTextColor(Color.parseColor("#9000FF00"));
//            textV5.setTextColor(Color.parseColor("#9000FF00"));
//        });
//        c4.setOnClickListener(view -> {
//            textV4.setTextColor(Color.parseColor("#FFA500"));
//            textV6.setTextColor(Color.parseColor("#9000FF00"));
//            textV1.setTextColor(Color.parseColor("#9000FF00"));
//            textV2.setTextColor(Color.parseColor("#9000FF00"));
//            textV3.setTextColor(Color.parseColor("#9000FF00"));
//            textV5.setTextColor(Color.parseColor("#9000FF00"));
//        });
//        c5.setOnClickListener(view -> {
//            textV5.setTextColor(Color.parseColor("#FFA500"));
//            textV6.setTextColor(Color.parseColor("#9000FF00"));
//            textV1.setTextColor(Color.parseColor("#9000FF00"));
//            textV2.setTextColor(Color.parseColor("#9000FF00"));
//            textV3.setTextColor(Color.parseColor("#9000FF00"));
//            textV4.setTextColor(Color.parseColor("#9000FF00"));
//        });
//        c6.setOnClickListener(view -> {
//            textV6.setTextColor(Color.parseColor("#FFA500"));
//            textV5.setTextColor(Color.parseColor("#9000FF00"));
//            textV1.setTextColor(Color.parseColor("#9000FF00"));
//            textV2.setTextColor(Color.parseColor("#9000FF00"));
//            textV3.setTextColor(Color.parseColor("#9000FF00"));
//            textV4.setTextColor(Color.parseColor("#9000FF00"));
//        });
//
//        if (pageNumber1 % 2 == 0) {
//            year+=pageNumber1/2;
//            yearV.setText(year + "");
//            LocalDate lll = LocalDate.of(2022, Month.JANUARY, 1);
//            final DateTimeFormatter ddd = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
//            String months = ddd.format(lll);
//            textV1.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV2.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV3.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV4.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV5.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV6.setText(months);
//        } else if (pageNumber1 % 2 == 1) {
//            year+=pageNumber1/2;
//            yearV.setText(year + "");
//            LocalDate lll = LocalDate.of(2022, Month.JULY, 1);
//            final DateTimeFormatter ddd = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
//            String months = ddd.format(lll);
//            textV1.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV2.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV3.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV4.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV5.setText(months);
//            lll = lll.plusMonths(1);
//            months = ddd.format(lll);
//            textV6.setText(months);
//        }
//        return result;
//    }
//}
