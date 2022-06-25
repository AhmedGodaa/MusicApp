package com.examplez.musicapp.fragments;

import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.examplez.musicapp.R;
import com.examplez.musicapp.adapters.MusicAdapter;
import com.examplez.musicapp.databinding.FragmentSongsBinding;
import com.examplez.musicapp.databinding.LayoutPlayerBinding;
import com.examplez.musicapp.listeners.MusicListener;
import com.examplez.musicapp.models.Music;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import me.tankery.lib.circularseekbar.CircularSeekBar;


public class SongsFragment extends Fragment implements MusicListener {


    private FragmentSongsBinding binding;
    public  MusicAdapter musicAdapter;
    private int position;
    private LayoutPlayerBinding playerBinding;
    private BottomSheetDialog bottomSheetDialog;
    private ObjectAnimator anim;
    static MediaPlayer mediaPlayer;
    static Uri uri;
    private Handler handler;
    Thread playThread, prevThread, nextThread;
    int seekBarMax;


    public SongsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSongsBinding.inflate(getLayoutInflater());
        setRecyclerView();
        addPlayerDialog();


        return binding.getRoot();
    }

    private void setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            musicAdapter = new MusicAdapter(getContext(), musicFiles, this);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            binding.recyclerView.setAdapter(musicAdapter);

        }

    }

    private void addPlayerDialog() {
        playerBinding = LayoutPlayerBinding.inflate(getLayoutInflater());
        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);


    }


    @Override
    public void onMusicClicked(Music musicFile, int position) {
        uri = Uri.parse(musicFile.getPath());
        bottomSheetDialog.setContentView(playerBinding.getRoot());
        bottomSheetDialog.show();
        handler = new Handler();

        setMusic();
        setSeekBar();
        setMusicData(musicFile);
        setAnimation();


    }

    @Override
    public void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void setMusic() {
        if (musicFiles != null) {
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), uri);
        mediaPlayer.start();
        playerBinding.seekBar.setMax(seekBarMax);

    }

    private void setMusicData(Music musicFile) {
        playerBinding.songName.setText(musicFile.getTitle());
        playerBinding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
        metaData(uri);
    }

    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playerBinding.btnPlayPause.setOnClickListener(view -> buttonPlayPause());
            }


        };
        playThread.start();
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playerBinding.btnNext.setOnClickListener(view -> buttonNext());
            }


        };
        nextThread.start();

    }

    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playerBinding.btnPrevious.setOnClickListener(view -> buttonPrevious());
            }


        };
        prevThread.start();
    }

    private void buttonPlayPause() {
        if (mediaPlayer.isPlaying()) {
            anim.pause();
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_play);
            playerBinding.seekBar.setMax(seekBarMax);
            mediaPlayer.pause();
//
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
//

        } else {
            anim.resume();
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            playerBinding.seekBar.setMax(seekBarMax);
            mediaPlayer.start();


            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private void buttonNext() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % musicFiles.size());
            uri = Uri.parse(musicFiles.get(position).getPath());
            metaData(uri);
            mediaPlayer = MediaPlayer.create(getContext(), uri);
            playerBinding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
            playerBinding.songName.setText(musicFiles.get(position).getTitle());

            playerBinding.seekBar.setMax(seekBarMax);
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();


        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % musicFiles.size());
            uri = Uri.parse(musicFiles.get(position).getPath());
            metaData(uri);
            mediaPlayer = MediaPlayer.create(getContext(), uri);
            playerBinding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
            playerBinding.songName.setText(musicFiles.get(position).getTitle());
            playerBinding.seekBar.setMax(seekBarMax);

//
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_play);


        }
    }

    private void buttonPrevious() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? musicFiles.size() - 1 : (position - 1));
            uri = Uri.parse(musicFiles.get(position).getPath());
            metaData(uri);
            mediaPlayer = MediaPlayer.create(getContext(), uri);
            playerBinding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));
            playerBinding.songName.setText(musicFiles.get(position).getTitle());
            playerBinding.seekBar.setMax(seekBarMax);
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();


        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? musicFiles.size() - 1 : (position - 1));
            uri = Uri.parse(musicFiles.get(position).getPath());
            metaData(uri);
            mediaPlayer = MediaPlayer.create(getContext(), uri);
            playerBinding.tvDurationTotal.setText(formattedTime(mediaPlayer.getDuration() / 1000));


            playerBinding.songName.setText(musicFiles.get(position).getTitle());
            playerBinding.seekBar.setMax(seekBarMax);


            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_play);


        }

    }

    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;

        if (art != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(playerBinding.songImageContainer);


            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            Palette.from(bitmap).generate(palette -> {
                assert palette != null;
                Palette.Swatch swatch = palette.getDominantSwatch();
                GradientDrawable gradientDrawable;
                if (swatch != null) {

                    gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{swatch.getRgb(), 0x00000000});


                } else {
                    gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{0xff000000, 0xff000000});

                }
                playerBinding.viewBackground.setBackground(gradientDrawable);
            });


        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.ic_launcher_background)
                    .into(playerBinding.songImageContainer);
        }

    }

    private void setSeekBar() {

        playerBinding.seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float i, boolean b) {
                if (mediaPlayer != null && b) {
                    int c = (int) i;
                    mediaPlayer.seekTo(c * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    playerBinding.seekBar.setProgress(mCurrentPosition);
                    playerBinding.tvDurationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });


    }

    public static String formattedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void setAnimation() {
        anim = ObjectAnimator.ofFloat(playerBinding.songImageContainer, "rotation", 0, 360);
        anim.setDuration(15000);
        anim.setRepeatCount(5);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatMode(ObjectAnimator.RESTART);
        anim.start();

    }
}