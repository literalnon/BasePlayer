package com.example.donald.testapp.Fragment;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.donald.testapp.BaseView;
import com.example.donald.testapp.MainView;
import com.example.donald.testapp.Presenter;
import com.example.donald.testapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayerViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerViewFragment extends Fragment implements BaseView{

    private OnFragmentInteractionListener mListener;

    private MainView mainView;
    private SeekBar sbVolume;
    private SeekBar sbPassed;
    private Button btnPlay;
    private TextView tvFileName;
    private Handler refreshProgress;
    private Presenter presenter;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sbPassed.setProgress(presenter.getCurrentPosition());
            refreshProgress.postDelayed(runnable, 1000);
        }
    };

    public PlayerViewFragment() {
        refreshProgress = new Handler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlayerViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerViewFragment newInstance() {
        PlayerViewFragment fragment = new PlayerViewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainContainer = inflater.inflate(R.layout.fragment_player_view, container, false);

        mainContainer.findViewById(R.id.btnList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.HidePlayerFragment();
            }
        });

        sbVolume = (SeekBar)mainContainer.findViewById(R.id.sbVolume);
        sbVolume.setMax(presenter.getMaxVolume());
        sbVolume.setProgress(presenter.getCurVolume());
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbPassed = (SeekBar)mainContainer.findViewById(R.id.sbPassed);
        sbPassed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                presenter.setProgress(seekBar.getProgress());
            }
        });

        btnPlay = (Button) mainContainer.findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.playerIsPlaying()) {
                    presenter.playerStop();
                    btnPlay.setText("play");
                }else{
                    presenter.playerStart();
                    btnPlay.setText("pause");
                }
            }
        });

        Button btnRepeat = (Button)mainContainer.findViewById(R.id.btnRepeat);

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setLooping(presenter.playerIsLooping() ? false : true);
            }
        });

        Button btnNext = (Button)mainContainer.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.incCurPosition();
                presenter.play();
            }
        });

        Button btnPrev= (Button)mainContainer.findViewById(R.id.btnPrev);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.decCurPosition();
                presenter.play();
            }
        });

        tvFileName = (TextView) mainContainer.findViewById(R.id.tvFileName);


        return mainContainer;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainView = (MainView)context;
        presenter = (Presenter)mainView.getPresenter();
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public MainView getMainView() {
        return mainView;
    }

    @Override
    public void play() {
        sbPassed.setMax(presenter.getDuration());
        sbPassed.setProgress(0);
        refreshProgress.postDelayed(runnable, 1000);
        btnPlay.setText("pause");
        tvFileName.setText(presenter.getName());
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
        void onFragmentInteraction(Uri uri);
    }
}
