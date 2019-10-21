package ca.ubc.ece.cpen221.mp1;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Random;
import java.util.Arrays;

public class BasicTests {
    final static int samplingRate=44100;

    @Test
    public void testCreateWave() {
        double[] lchannel = {1.0, -1.0};
        double[] rchannel = {1.0, -1.0};
        SoundWave wave = new SoundWave(lchannel, rchannel);
        double[] lchannel1 = wave.getLeftChannel();
        Assert.assertArrayEquals(lchannel, lchannel1, 0.00001);
        double[] rchannel1 = wave.getRightChannel();
        Assert.assertArrayEquals(rchannel, rchannel1, 0.00001);
    }




    @Test
    //Tests that a created echo wave is the length of the wave plus some delta
    public void testAddEcho(){
        int delta = 22000;
        SoundWave wave = new SoundWave(440,0,1,2);
        SoundWave EchoWave = wave.addEcho(delta, 1);
        Assert.assertEquals(wave.getLeftChannel().length + delta,EchoWave.getLeftChannel().length);


    }

    @Test
    //Tests adding two out of phase waves results in zero
    public void testAdd(){

        SoundWave wave1 = new SoundWave(440,0,1,3);
        SoundWave wave2 = new SoundWave(440,Math.PI,1,3);
        SoundWave addWave = wave1.add(wave2);

        for(double i : addWave.getLeftChannel()) {
            Assert.assertEquals(0.0, i, 0.0001);
        }
    }

    @Test
    //Tests that when adding two waves they are of the same length
    public void testAdd2(){

        SoundWave wave1 = new SoundWave(440,0,1,3);
        SoundWave wave2 = new SoundWave(440,Math.PI,1,3);
        SoundWave addWave = wave1.add(wave2);

        Assert.assertEquals(wave1.getLeftChannel().length, addWave.getLeftChannel().length);

    }

    @Test
    //Tests that adding two in phase waves results in a doubled amplitude
    public void testAdd3(){

        SoundWave wave1 = new SoundWave(440,0,0.2,3);
        SoundWave addWave = wave1.add(wave1);
        double amp = addWave.getAmpLCH();
        Assert.assertEquals(0.4, amp, 0.01);

    }

    @Test
    //Tests that we get frequency out of the function
    public void testHighFreqComp(){
        SoundWave wave1 = new SoundWave(440,0,0.2,1);
        assertEquals(440, wave1.highAmplitudeFreqComponent(), 0.01);
    }

    @Test
    //Tests that the higher frequency is returned for two waves of the same amplitude
    public void testHighFreqComp1(){
        SoundWave wave1 = new SoundWave(440,0,0.5,1);
        SoundWave wave2 = new SoundWave(1100,0,0.5,1);
        SoundWave wave = wave1.add(wave2);
        assertEquals(1100, wave.highAmplitudeFreqComponent(), 0.01);
    }

    @Test
    //Tests that the larger amplitude frequency is returned even if its of lower frequency
    public void testHighFreqComp2(){
        SoundWave wave1 = new SoundWave(440,0,0.4,1);
        SoundWave wave2 = new SoundWave(550,0,0.2,1);
        assertEquals(440, wave1.add(wave2).highAmplitudeFreqComponent(), 0.01);
    }

    @Test
    //checks that the larger wave of larger frequency is returned
    public void testHighFreqComp3(){
        SoundWave wave1 = new SoundWave(440,0,0.2,1);
        SoundWave wave2 = new SoundWave(550,0,0.4,1);
        assertEquals(550, wave1.add(wave2).highAmplitudeFreqComponent(), 0.01);
    }

    @Test
    //tests that 220Hz is removed so the high amplitude component only returns 1100Hz
    public  void testHighPassFilter(){
        SoundWave wave1 = new SoundWave(220,0,1,1);
        SoundWave wave2 = new SoundWave(1100,0,0.5,1);
        SoundWave wave = wave1.add(wave2);
        //filter out frequency below 500Hz
        wave = wave.highPassFilter(1, Math.pow((Math.PI * 1000), -1));

        assertEquals(1100, wave.highAmplitudeFreqComponent(), 0.1);

    }

    @Test
    //Checks that 440hz is removed and that the higher amplitude frequency wave is returned from high amplitude frequency components
    public  void testHighPassFilter2(){
        SoundWave wave1 = new SoundWave(440,0,1,1);
        SoundWave wave2 = new SoundWave(1100,0,0.5,1);
        SoundWave wave3 = new SoundWave(660,0,1,1);
        SoundWave wave = wave1.add(wave2.add(wave3));
        //filter out frequency below 500Hz
        wave = wave.highPassFilter(1, Math.pow((Math.PI * 1000), -1));

        assertEquals(660, wave.highAmplitudeFreqComponent(), 0.1);

    }


    @Test
    public void testFourrier(){
        SoundWave wave = new SoundWave(440,0,1,1);
        double[][] freqA = wave.dfTransform();
        int max = 0;
        for(int i = 0; i < freqA.length; i++){
            if(freqA[i][1] > freqA[max][1]){
                max = i;
            }
        }

        assertEquals(440, freqA[max][0], 0.1);
    }

    @Test
    public void testFourrier2(){
        SoundWave wave1 = new SoundWave(440,0,0.2,1);
        SoundWave wave2 = new SoundWave(550,0,0.3,1);
        SoundWave wave = wave1.add(wave2);
        double[][] freqA = wave.dfTransform();
        int max = 0;
        for(int i = 0; i < freqA.length; i++){
            if(freqA[i][1] > freqA[max][1]){
                max = i;
            }
        }

        assertEquals(550,freqA[max][0], 0.1);
    }

    @Test
    public void testFourrier3(){
        SoundWave wave1 = new SoundWave(440,0,0.4,1);
        SoundWave wave2 = new SoundWave(550,0,0.2,1);
        SoundWave wave = wave1.add(wave2);
        double[][] freqA = wave.dfTransform();
        int max = 0;
        for(int i = 0; i < freqA.length; i++){
            if(freqA[i][1] > freqA[max][1]){
                max = i;
            }
        }

        assertEquals(440, freqA[max][0], 0.1);
    }

    @Test
    public void testSineduration(){
        final int samplingrate = 44100;

        double duration = Math.random()*(20);
        SoundWave wave = new SoundWave(10,14,13,duration);
        int lchannelsize = wave.getLeftChannel().length;
        int rchannelsize = wave.getRightChannel().length;

        Assert.assertEquals((int)(duration*samplingrate),lchannelsize,0.00001);
        Assert.assertEquals((int)(duration*samplingrate),rchannelsize,0.00001);
    }
    /*
    Checks validity of Sine function.
    Generates two waves with phase difference 2pi and compares them
     */
    @Test
    public void testSinephase(){
        double phase = Math.random()*(200);

        SoundWave wave = new SoundWave(10,phase,13,5);
        SoundWave wave2= new SoundWave (10,phase + Math.PI*2,13,5);


        Assert.assertArrayEquals(wave.getLeftChannel(),wave2.getLeftChannel(),0.00001);
        Assert.assertArrayEquals(wave.getRightChannel(),wave2.getRightChannel(),0.00001);



    }
    /*
    Checks length of appended wave
     */
    @Test
    public void appendTest1(){
        final int samplingrate = 44100;
        double duration1 = Math.random()*10, duration2 = Math.random()*10;
        int length1 = (int)(duration1*samplingrate), length2 = (int)(duration2*samplingrate);

        SoundWave wave1 = new SoundWave(10,0,1,duration1);
        SoundWave wave2 = new SoundWave(11,0,2,duration2);

        wave1.append(wave2);

        Assert.assertEquals(length1+length2, wave1.getLeftChannel().length,0.00001);
        Assert.assertEquals(length1+length2, wave1.getRightChannel().length,0.00001);

    }
    /*
    Evaluates index at first wave,boundary and second wave
     */
    @Test
    public void appendTest2(){
        double duration1 = Math.random()*10, duration2 = Math.random()*10;
        int length1 = (int)(duration1*samplingRate), length2 = (int)(duration2*samplingRate);

        final int samplingrate = 44100;

        SoundWave wave1 = new SoundWave(10,0,1,duration1);
        SoundWave wave2 = new SoundWave(11,0,2,duration2);

        double[] channel1 = wave1.getLeftChannel();
        double[] channel2 = wave2.getLeftChannel();

        wave1.append(wave2);

        double[] channela1 = new double[length1];
        double[] channela2 = new double[length2];

        System.arraycopy(wave1.getLeftChannel(),0,channela1,0,length1);
        System.arraycopy(wave1.getLeftChannel(),length1,channela2,0,length2);


        Assert.assertArrayEquals(channela1,channel1,0.00001);
        Assert.assertArrayEquals(channela2,channel2,0.00001);

    }
    /*
    Tests Scale

     */

    @Test
    public void testScale(){
        double scale = Math.random();

        SoundWave wave = new SoundWave(12,13,1,15);
        wave.scale(scale);
        Assert.assertEquals(scale,wave.getAmpLCH(),.00001);
        Assert.assertEquals(scale,wave.getAmpRCH(),.00001);

    }
    /*
    Echo test - on adding echo length of wave should increase by offset
     */
    @Test
    public void echoTest1(){
        final int samplingrate = 44100;
         double duration = Math.random()*10;

        SoundWave wave = new SoundWave(12,13,1,duration);

        wave = wave.addEcho(22000,.5);


        Assert.assertEquals((int)(duration*samplingRate) + 22000,wave.getLeftChannel().length);



    }
    /*
    Waves should contain themselves
     */
    @Test
    public void containTest1(){
        double duration = Math.random()*0.5;
        SoundWave wave = new SoundWave(12,13,1,duration);
        Assert.assertEquals(true,wave.contains(wave));

    }
    /*
    Waves contain all appended waves
   */
    @Test
    public void containTest2(){
        double duration = Math.random()*0.1;
        SoundWave wave = new SoundWave(12,13,1,duration);
        SoundWave awave = new SoundWave(12,14,1,duration*0.5);

        wave.append(awave);


        Assert.assertEquals(true,wave.contains(awave));

    }

    /*
    Waves should contain appended waves
     */
    @Test
    public void containTest3(){
        double duration = Math.random()*0.1;
        SoundWave wave = new SoundWave(12,13,1,duration);
        SoundWave awave = new SoundWave(12,14,1,duration*0.5);

        wave.append(awave);


        Assert.assertEquals(true,wave.contains(awave));

    }
    /*
    Waves cannot contain waves larger thanselves
    */
    @Test
    public void containTest4(){
        double duration = Math.random()*0.1;
        SoundWave wave = new SoundWave(12,13,1,duration);
        SoundWave wave2 = new SoundWave(12,14,1,duration*25);




        Assert.assertEquals(false,wave.contains(wave2));

    }
    /*
        Scaled wave test
    */
    @Test
    public void containTest5(){
        double duration = Math.random()*0.1;
        SoundWave wave = new SoundWave(12,13,1,duration);
        SoundWave wave2 = new SoundWave(12,14,1,duration*0.5);
        wave.append(wave2);

        wave2.scale(.4);


        Assert.assertEquals(true,wave.contains(wave2));




    }



}
