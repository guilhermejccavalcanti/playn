/**
 * Copyright 2012 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.ios;

import cli.MonoTouch.AVFoundation.AVAudioPlayer;
import cli.MonoTouch.Foundation.NSError;
import cli.MonoTouch.Foundation.NSUrl;
import playn.core.Asserts;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import playn.core.Sound;

/**
 * An implementation of Sound using the AVAudioPlayer.
 */
class IOSSound implements Sound
{
  private AVAudioPlayer player;

  public IOSSound (String path) {
    NSError[] error = new NSError[1];
    player = AVAudioPlayer.FromUrl(NSUrl.FromFilename(path), error);
    if (error[0] != null) {
        PlayN.log().warn("Error loading sound [" + path + ", " + error[0] + "]");
        return;
    }
    player.PrepareToPlay();
  }

  @Override
  public boolean play() {
    Asserts.check(player != null, "Playing a null player");
    player.set_CurrentTime(0);
    return player.Play();
  }

  @Override
  public void stop() {
    Asserts.check(player != null, "Stopping a null player");
    player.Pause();
    player.set_CurrentTime(0);
  }

  @Override
  public void setLooping(boolean looping) {
    Asserts.check(player != null, "Setting looping on a null player");
    player.set_NumberOfLoops(looping ? -1 : 0);
  }

  @Override
  public void setVolume(float volume) {
    Asserts.check(player != null, "Setting volume on a null player");
    player.set_Volume(volume);
  }

  @Override
  public boolean isPlaying() {
    return player != null && player.get_Playing();
  }

  @Override
  public void addCallback(ResourceCallback<? super Sound> callback) {
    if (player != null) {
      // non-null players are always ready
      callback.done(this);
    }
  }

  public void dispose() {
    if (player != null) {
      player.Dispose();
      player = null;
    }
  }
}
