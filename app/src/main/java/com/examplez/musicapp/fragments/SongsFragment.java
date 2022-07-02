package com.examplez.musicapp.fragments;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.examplez.musicapp.activities.MainActivity.musicFiles;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

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
import com.examplez.musicapp.listeners.ActionPlaying;
import com.examplez.musicapp.listeners.MusicListener;
import com.examplez.musicapp.models.Constants;
import com.examplez.musicapp.models.Music;
import com.examplez.musicapp.utils.MusicService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import me.tankery.lib.circularseekbar.CircularSeekBar;


public class SongsFragment extends Fragment implements MusicListener, ServiceConnection, ActionPlaying {


    private FragmentSongsBinding binding;
    public MusicAdapter musicAdapter;
    private int position;
    private LayoutPlayerBinding playerBinding;
    private BottomSheetDialog bottomSheetDialog;
    private ObjectAnimator anim;
    static Uri uri;
    private Handler handler;
    Thread playThread, prevThread, nextThread;
    int seekBarMax;
    MusicService musicService;
    int songPosition;
    MediaSessionCompat mediaSessionCompat;


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
        mediaSessionCompat = new MediaSessionCompat(requireContext(), "My Audio ");
        musicServiceStart();

        return binding.getRoot();
    }

    @Override
    public void onClick(Music music, int position) {
        songPosition = position;
        uri = Uri.parse(music.getPath());
        bottomSheetDialog.setContentView(playerBinding.getRoot());
        bottomSheetDialog.show();
        handler = new Handler();

        if (musicService != null) {
            musicService.playMedia(position);
            setSeekBar();
            setMusicData(music);
            setAnimation();
        } else {
            Toast.makeText(requireContext(), "Music Service Null", Toast.LENGTH_SHORT).show();
        }


    }

    private void musicServiceStart() {
        //        showNotification(R.drawable.ic_pause);
        Intent intent = new Intent(requireContext(), MusicService.class);
        requireActivity().bindService(intent, this, BIND_AUTO_CREATE);
        intent.putExtra(Constants.SERVICE_STARTER, true);
        requireActivity().startService(intent);
        Toast.makeText(requireContext(), "" + musicService, Toast.LENGTH_SHORT).show();

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
    public void onResume() {

        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
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

    public void buttonPlayPause() {
        if (musicService.isPlaying()) {
            anim.pause();
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_play);
            playerBinding.seekBar.setMax(seekBarMax);
            musicService.pause();
//
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
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
            musicService.start();


            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    public void buttonNext() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            position = ((position + 1) % musicFiles.size());
            uri = Uri.parse(musicFiles.get(position).getPath());
            metaData(uri);
            musicService.createMediaPlayer(position);
            playerBinding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));
            playerBinding.songName.setText(musicFiles.get(position).getTitle());

            playerBinding.seekBar.setMax(seekBarMax);
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            musicService.start();


        } else {
            musicService.stop();
            musicService.release();
            position = ((position + 1) % musicFiles.size());
            uri = Uri.parse(musicFiles.get(position).getPath());
            metaData(uri);
            musicService.createMediaPlayer(position);
            playerBinding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));
            playerBinding.songName.setText(musicFiles.get(position).getTitle());
            playerBinding.seekBar.setMax(seekBarMax);

//
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_play);


        }
    }

    public void buttonPrevious() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            position = ((position - 1) < 0 ? musicFiles.size() - 1 : (position - 1));
            uri = Uri.parse(musicFiles.get(position).getPath());
            metaData(uri);
            musicService.createMediaPlayer(position);
            playerBinding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));
            playerBinding.songName.setText(musicFiles.get(position).getTitle());
            playerBinding.seekBar.setMax(seekBarMax);
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            musicService.start();


        } else {
            musicService.stop();
            musicService.release();
            position = ((position - 1) < 0 ? musicFiles.size() - 1 : (position - 1));
            uri = Uri.parse(musicFiles.get(position).getPath());
            metaData(uri);
            musicService.createMediaPlayer(position);
            playerBinding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));


            playerBinding.songName.setText(musicFiles.get(position).getTitle());
            playerBinding.seekBar.setMax(seekBarMax);


            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        playerBinding.seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playerBinding.btnPlayPause.setImageResource(R.drawable.ic_play);


        }

    }

    private void metaData(Uri uri) {
        byte[] art = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(uri.toString());
            art = retriever.getEmbeddedPicture();

        } catch (Exception e) {

        }
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
                if (musicService != null && b) {
                    int c = (int) i;
                    musicService.seekTo(c * 1000);
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
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    playerBinding.seekBar.setProgress(mCurrentPosition);
                    playerBinding.tvDurationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });


    }

    private void setMusicData(Music musicFile) {
        playerBinding.songName.setText(musicFile.getTitle());
        playerBinding.tvDurationTotal.setText(formattedTime(musicService.getDuration() / 1000));
        metaData(uri);
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

    private String formattedTime(int mCurrentPosition) {
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

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
        musicService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }
}