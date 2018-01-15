package com.example.trancoso.flashcard;

//import android.app.Fragment;
//import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
//import android.content.CursorLoader;
//import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartesFragment extends Fragment {

    private static final String AUTHORITY = "authority";
    private static final String PATH = "path";
    private static final String COL_AFFICHEE = "colonne_affichee";
    private static final String COL_AFFICHEE2 = "colonne_affichee2";
    private static final String COL_RECHERCHEE = "colonne_recherchee";
    private static final String VALEUR = "valeur";
    private SimpleCursorAdapter mAdaper;

    private static final String TAG = "CartesFragment";
    private String mAthority, mColAffichee, mColAffichee2, mColRecherchee;
    private String[] mPath;
    private long mValeur;
    private LoaderManager.LoaderCallbacks<Cursor> mCallback;
    private OnFragmentInteractionListener mListener;
    private ListView mList;

    public CartesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param authority         authority de ContentProvider.
     * @param path              les elements de path de uri
     * @param colonneAffichee   le nom de la colonne affichee dans la liste
     * @param colonneAffichee2   le nom de la colonne affichee dans la liste
     *
     * @param colonneRecherchee
     * @param valeur            on affiche que les itmes de a table
     *                          qui satisfont le critere colonneRecherchee=valeur
     * @return A new instance of fragment SupprimerFragment.
     */
    //
    public static CartesFragment newInstance(String authority,
                                                String[] path,
                                                String colonneAffichee,/* colonne affichee
                                                                     * dans la liste */
                                                String colonneAffichee2,
                                                String colonneRecherchee,
                                                long valeur) {
        CartesFragment fragment = new CartesFragment();
        Bundle args = new Bundle();
        args.putString(AUTHORITY, authority);
        args.putStringArray(PATH, path);
        args.putString(COL_AFFICHEE, colonneAffichee);
        args.putString(COL_AFFICHEE2, colonneAffichee2);
        args.putString(COL_RECHERCHEE, colonneRecherchee);
        args.putLong(VALEUR, valeur);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d(TAG,"getArguments not null");
            mAthority = getArguments().getString(AUTHORITY);
            mPath = getArguments().getStringArray(PATH);
            mColAffichee = getArguments().getString(COL_AFFICHEE);
            mColAffichee2 = getArguments().getString(COL_AFFICHEE2);
            mColRecherchee = getArguments().getString(COL_RECHERCHEE);
            mValeur = getArguments().getLong(VALEUR);
        } else {
            throw new RuntimeException("cannot work without arguments");
        }




        /* definir Callback pour les titres */
        mCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id_loader, Bundle args) {
                Uri.Builder builder = (new Uri.Builder()).scheme("content")
                        .authority(mAthority);
                for (String element : mPath) {
                    builder.appendPath(element);
                }
                builder = ContentUris.appendId(builder, mValeur);

                return new CursorLoader(getActivity(), builder.build(),
                        null, //new String[]{"_id", mColAffichee},
                        null, //mColRecherchee + " = " + mValeur,
                        null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mAdaper.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                Log.d("loader reset", "ok");
                mAdaper.swapCursor(null);
            }
        };/* fin Callback pour les  titres */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cartes, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mList = (ListView) getActivity().findViewById(R.id.listCartes);
        if( mList == null){
            Log.d(TAG, "mList is null");
            return;
        }
        mAdaper = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                null,/* Cursor null initalement */
                new String[]{"recto", "verso"},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        mList.setAdapter(mAdaper);
        getLoaderManager().initLoader(1, null, mCallback);


        /*Button button = (Button) getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG,"begin suppression");
                long[] ids = mList.getCheckedItemIds();
                int res = 0;
                if (ids.length == 0) {
                    //Log.d(TAG, "liste a supprimer vide");
                    return;
                }

                for (long id : ids) {
                    //Log.d(TAG, "supprimer id =" + id );
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("content")
                            .authority(mAthority)
                            .appendPath(mPath[0]);

                    *//* ajouter id du livre a supprimer a la fin de uri *//*
                    ContentUris.appendId(builder, id);
                    Uri uri = builder.build();

                    int j = getActivity().getContentResolver()
                            .delete(uri, null, null);
                }
                *//* apres la suppression de titres mettre a jour la liste
                   de titres, id_author ne change pas *//*
                Log.d(TAG,"restart loader");
                getLoaderManager().restartLoader(1, null, mCallback);
            }
        });*/
    }/* fin onActivityCreated() */



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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        //void onFragmentInteraction(Uri uri);
    }
}
