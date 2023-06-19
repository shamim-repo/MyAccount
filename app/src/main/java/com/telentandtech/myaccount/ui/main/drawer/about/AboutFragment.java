package com.telentandtech.myaccount.ui.main.drawer.about;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telentandtech.myaccount.R;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView aboutTextView = view.findViewById(R.id.about_textView);
        aboutTextView.setText(aboutAppText);
    }

    private String aboutAppText = "My Account is a user-friendly app designed to streamline the process of managing student fees. Whether you're an educational institution, a private tutor, or even a parent managing fees for multiple students, this app is here to simplify your life.\n\n" +
            "Key Features:\n\n" +
            "- Student Management: Easily add and manage student profiles within the app. Input essential details such as student name, contact information, and relevant notes.\n\n" +
            "- Class and Group Management: Organize students into classes or groups for efficient fee management. Add and manage classes or groups, and assign students to them.\n\n" +
            "- Fee Management: Add and manage fees effortlessly. Set up fee structures for different classes or groups, specifying amounts, due dates, and any additional charges. FeeManager allows you to customize fee details to match your specific requirements.\n\n" +
            "- Payment Management: Keep track of fee payments seamlessly. Record payments made by students. Easily view payment history.\n\n" +
            "- Attendance Management: Monitor student attendance within the app. Mark students as present, absent, or on leave for individual classes or group sessions. Maintain accurate attendance records to ensure transparent communication with students and parents.\n\n" +
            "- Export to Excel or PDF: Generate comprehensive reports for easy data analysis and sharing. Export payment lists, attendance records, or student profiles in Excel or PDF format. Seamlessly share reports with school administrators, faculty, or parents, ensuring transparent communication.\n\n" +
            "- User-Friendly Interface: FeeManager offers a clean and intuitive interface, making it easy for users of all backgrounds to navigate and utilize its features. The app provides a hassle-free experience, saving you time and effort in managing student fees.\n\n" +
            "- Data Security: Your data is secure with FeeManager. We prioritize the privacy and confidentiality of your information. Your student and fee data are encrypted and protected, ensuring that sensitive data remains safe and accessible only to authorized users.\n\n" +
            "My Account is the ultimate tool for efficiently managing student fees, attendance, and payments. Say goodbye to manual calculations and paperwork. Download FeeManager today and experience the convenience of simplified student fee management, allowing you to focus on what truly matters - educating and nurturing young minds.";

}