//package com.example.contactdeleter;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ContactsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class ContactsFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String CONTACTS = "contacts";
//    private ArrayList<Contact> contacts;
//
//    public ContactsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param names Parameter 1.
//     * @param numbers Parameter 2.
//     * @return A new instance of fragment ContacsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ContactsFragment newInstance(ArrayList<String> names, ArrayList<String> numbers) {
//        ContactsFragment fragment = new ContactsFragment();
//        Bundle args = new Bundle();
//        args.putStringArrayList(NAMES, names);
//        args.putStringArrayList(NUMBERS, numbers);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            names = getArguments().getStringArrayList(NAMES);
//            numbers = getArguments().getStringArrayList(NUMBERS);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_contacs, container, false);
//
//        ListView listView = view.findViewById(R.id.list);
//        ContactsAdapter adapter = new ContactsAdapter(getActivity(), names, numbers);
//        listView.setAdapter(adapter);
//        return view;
//    }
//}