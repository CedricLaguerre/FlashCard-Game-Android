package com.example.trancoso.flashcard;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListeJeuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListeJeuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListeJeuFragment extends ListFragment {
    private static final String AUTHORITY = "authority";
    private static final String TABLE = "table";
    private static final String COLUMN = "column";
    private OnFragmentInteractionListener mListener;
    private SimpleCursorAdapter adapter;
    private String mAuthority, mTable, mColumn;
    private static final String LOG = "ListeJeuFragment";

    public ListeJeuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListeJeuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListeJeuFragment newInstance(String authority, String table, String column) {
        ListeJeuFragment fragment = new ListeJeuFragment();
        Bundle args = new Bundle();
        args.putString(AUTHORITY, authority);
        args.putString(TABLE, table);
        args.putString(COLUMN, column);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthority = getArguments().getString(AUTHORITY);
        mTable = getArguments().getString(TABLE);
        mColumn = getArguments().getString(COLUMN);

        adapter = new SimpleCursorAdapter(
                getActivity(),/*context*/
                android.R.layout.simple_list_item_1,
                null, /*Cursor - null initialement */
                new String[]{mColumn},
                new int[]{android.R.id.text1}, 0);
            /* dans ListFragment utiliser setListAdapter() au lieu de setAdapter() */
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri = (new Uri.Builder()).scheme("content")
                        .authority(mAuthority)
                        .appendPath(mTable)
                        .build();
                Log.d(LOG, "onCreateLoader uri=" + uri.toString());
                return new CursorLoader(getActivity(), uri,
                        new String[]{"_id", mColumn},
                        null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
                if (data != null) {
                    Log.d(LOG, "load finished taille=" + data.getCount() + "");
                } else {
                    Log.d(LOG, "load finished data null");
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapter.swapCursor(null);
            }
        });
    }

/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }*/

/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(LOG, "clicked dans la liste");
        if(mListener == null){
            Log.d(LOG,"onListItemClick mListener=null");
        }
        /*int indice = position;*/
        mListener.onIdSelection(id);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onIdSelection(long id);
    }
}
