package Client;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Client.Game.GameState;

public class MusicThread extends Thread {
//	Game game;
	private AudioInputStream inputStream;
	private Clip currentClip;

	public MusicThread() {
		updateSound(GameState.TITLE, false);
		start();
	}

	public void updateSound(GameState gamestate, boolean gameOver) {
		if (currentClip != null)
			currentClip.close();

		if (gamestate.equals(GameState.TITLE)) {
			try {
				currentClip = AudioSystem.getClip();
				inputStream = AudioSystem.getAudioInputStream(
						new File(getClass().getClassLoader().getResource("audio/title.wav").toURI()));
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (!gameOver) {
			try {
				currentClip = AudioSystem.getClip();
				inputStream = AudioSystem.getAudioInputStream(
						new File(getClass().getClassLoader().getResource("audio/gameMusic.wav").toURI()));
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				currentClip = AudioSystem.getClip();
				inputStream = AudioSystem.getAudioInputStream(
						new File(getClass().getClassLoader().getResource("audio/endGame.wav").toURI()));
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				currentClip.open(inputStream);
				currentClip.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
