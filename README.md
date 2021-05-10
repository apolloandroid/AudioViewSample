# AudioViewSample

Sample project of using AudioView. 

This view can be used for device microphone checking or other representing sound level on the screen task.

**Using**

in XML
```
<com.example.audioviewsample.AudioView
            android:id="@+id/audio_view"
            android:layout_width="230dp"
            android:layout_height="20dp"
            app:front_chunk_color="@color/default_front_chunk_color"
            app:back_chunk_color="@color/default_back_chunk_color"
            app:chunk_count="8"
            app:space_between_chunks="10dp"
            app:circle_chunk_radius="10dp"/>
            
 
In Activity/Fragment

You can execute this code in a timer, for example, every 100 milliseconds.

val audioView: AudioView = findViewById(R.id.audioView)
 
// in the timer redraw view
audioView.setVolume(currentVolume)   
 
 
Compatibility

Minimum Android SDK: AudioRecordView requires a minimum API level of 16.
