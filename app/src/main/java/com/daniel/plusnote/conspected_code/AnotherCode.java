package com.daniel.plusnote.conspected_code;

public class AnotherCode {
    //        if (notes.get(position).isIs_voice()) {
//            holder.seekbar.setVisibility(View.VISIBLE);
//            holder.audio_length.setVisibility(View.VISIBLE);
//            holder.play_btn.setVisibility(View.VISIBLE);
//            holder.playback_timer.setVisibility(View.VISIBLE);
//        } else {
//            holder.titleOut.setTextSize(21);
//        }
//        holder.audio_length.setText(notes.get(position).getAudio_length());
//        holder.seekbar.setProgress(0);
//        holder.play_btn.setOnClickListener(view -> {
//            if (isPlaying) {
//                holder.playback_timer.stop();
//                pauseAudio(holder);
//                holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//            } else {
//                if (!resumeAudio){
//                    holder.playback_timer.setBase(SystemClock.elapsedRealtime());
//                    holder.playback_timer.start();
//                    if (mediaPlayer != null){
//                        mediaPlayer.stop();
//                    }
//                    playAudio(notes.get(position).getImage_path(), holder);
//                    holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//                    resumeAudio = !resumeAudio;
//                } else {
//                    holder.playback_timer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
//                    holder.playback_timer.start();
//                    holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//                    resumeAudio(holder);
//                }
//            }
//            isPlaying = !isPlaying;
//        });
    //        holder.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                pauseAudio(holder);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                int progress = seekBar.getProgress();
//                mediaPlayer.seekTo(progress);
//                holder.playback_timer.setBase(SystemClock.elapsedRealtime() - mediaPlayer.getCurrentPosition());
//                if (resumeAudio){
//                    resumeAudio(holder);
//                } else {
//                    mediaPlayer = new MediaPlayer();
//                    String path = notes.get(holder.getAdapterPosition()).getImage_path();
//                    resumeAudio(path, holder);
//                }
//                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
//                    holder.playback_timer.stop();
//                    holder.playback_timer.setBase(SystemClock.elapsedRealtime());
//                    holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//                    isPlaying = false;
//                    resumeAudio = false;
//                    seekBar.setProgress(0);
//                    stopAudio();
//                });
//            }
//        });
    //    private void stopAudio() {
//        mediaPlayer.stop();
//        seekbarHandler.removeCallbacks(updateSeekbar);
//    }
//
//    private void pauseAudio(NoteViewHolder holder){
//        mediaPlayer.pause();
//        holder.playback_timer.stop();
//        elapsedMillis = SystemClock.elapsedRealtime() - holder.playback_timer.getBase();
//        seekbarHandler.removeCallbacks(updateSeekbar);
//        holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//    }
//
//    private void resumeAudio(NoteViewHolder holder){
//        mediaPlayer.start();
//        holder.playback_timer.start();
//        holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//        updateRunnable(holder);
//        seekbarHandler.postDelayed(updateSeekbar, 0);
//    }
//
//    private void resumeAudio(String path, NoteViewHolder holder){
//        try {
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        holder.playback_timer.start();
//        holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//        updateRunnable(holder);
//        seekbarHandler.postDelayed(updateSeekbar, 0);
//    }
//
//    private void playAudio(String path, NoteViewHolder holder) {
//        mediaPlayer = new MediaPlayer();
//
//        try {
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//        }
//
//        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
//            holder.playback_timer.stop();
//            holder.playback_timer.setBase(SystemClock.elapsedRealtime());
//            holder.play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//            isPlaying = false;
//            resumeAudio = false;
//            holder.seekbar.setProgress(0);
//            stopAudio();
//        });
//
//        holder.seekbar.setMax(mediaPlayer.getDuration());
//        seekbarHandler = new Handler();
//        updateRunnable(holder);
//        seekbarHandler.postDelayed(updateSeekbar, 0);
//    }
//
//    private void updateRunnable(NoteViewHolder holder){
//        updateSeekbar = new Runnable() {
//            @Override
//            public void run() {
//                holder.seekbar.setProgress(mediaPlayer.getCurrentPosition());
//                seekbarHandler.postDelayed(this, 100);
//            }
//        };
//    }
}
