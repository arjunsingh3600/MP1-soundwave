package ca.ubc.ece.cpen221.mp1;

import ca.ubc.ece.cpen221.mp1.utils.ComplexNumbers;
import ca.ubc.ece.cpen221.mp1.utils.HasSimilarity;
import javazoom.jl.player.StdPlayer;


import java.util.Arrays;

import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class SoundWave implements HasSimilarity<SoundWave> {

    // We are going to treat the number of samples per second of a sound wave
    // as a constant.
    // The best way to refer to this constant is as
    // SoundWave.SAMPLES_PER_SECOND.
    public static final int SAMPLES_PER_SECOND = 44100;

    // some representation fields that you could use
    private ArrayList<Double> lchannel = new ArrayList<>();
    private ArrayList<Double> rchannel = new ArrayList<>();
    private int samples = 0;

    /**
     * Create a new SoundWave using the provided left and right channel
     * amplitude values. After the SoundWave is created, changes to the
     * provided arguments will not affect the SoundWave.
     *
     * @param lchannel is not null and represents the left channel.
     * @param rchannel is not null and represents the right channel.
     */
    public SoundWave(double[] lchannel, double[] rchannel) {
        for(int i = 0; i < lchannel.length & i < rchannel.length; i++){
            this.lchannel.add(lchannel[i]);
            this.rchannel.add(rchannel[i]);
        }
    }

    /**
     * Creates a wave of length 1 with values zero in each channel
     */
    public SoundWave() {
        // that creates an empty wave

        this.lchannel.add(0.0);
        this.rchannel.add(0.0);
        this.samples++;

    }

    /**
     * Create a new sinusoidal sine wave,
     * sampled at a rate of 44,100 samples per second
     *
     * @param freq      the frequency of the sine wave, in Hertz
     * @param phase     the phase of the sine wave, in radians
     * @param amplitude the amplitude of the sine wave, 0 < amplitude <= 1
     * @param duration  the duration of the sine wave, in seconds
     */
    public SoundWave(double freq, double phase, double amplitude, double duration) {

        double[] waveArr = DoubleStream
                .iterate(0, i -> i + 1.0 / (double) SAMPLES_PER_SECOND)
                .limit((long) (SAMPLES_PER_SECOND * duration))
                .map(n -> amplitude * Math.sin(2 * Math.PI * freq * n + phase))
                .toArray();

        for (int i = 0; i < waveArr.length; i++) {
            this.lchannel.add(waveArr[i]);
            this.rchannel.add(waveArr[i]);
        }

    }

    /**
     * Obtain the left channel for this wave.
     * Changes to the returned array should not affect this SoundWave.
     *
     * @return an array that represents the left channel for this wave.
     */
    public double[] getLeftChannel() {
        double[] lchannelArr = new double[lchannel.size()];

        for (int i = 0; i < lchannel.size(); i++) {
            lchannelArr[i] = lchannel.get(i);
        }

        return lchannelArr; // change this
    }

    /**
     * Obtain the right channel for this wave.
     * Changes to the returned array should not affect this SoundWave.
     *
     * @return an array that represents the right channel for this wave.
     */
    public double[] getRightChannel() {
        double[] rchannelArr = new double[rchannel.size()];

        for (int i = 0; i < rchannel.size(); i++) {
            rchannelArr[i] = rchannel.get(i);
        }

        return rchannelArr; // change this
    }


    /**
     * A simple main method to play an MP3 file. Note that MP3 files should
     * be encoded to use stereo channels and not mono channels for the sound to
     * play out correctly.
     * <p>
     * One should try to get this method to work correctly at the start.
     * </p>
     *
     * @param args are currently ignored but you could be creative.
     */
    public static void main(String[] args) {


        StdPlayer.open("mp3/late.mp3");
        SoundWave sw = new SoundWave();
        while (!StdPlayer.isEmpty()) {
            double[] lchannel = StdPlayer.getLeftChannel();
            double[] rchannel = StdPlayer.getRightChannel();
            sw.append(lchannel, rchannel);
        }

        System.out.println(sw.getLeftChannel().length);

        SoundWave A = new SoundWave(390.5,0,0.8,5);
        SoundWave B = new SoundWave(550,0,0.8,1);

       // SoundWave C = A.add(B);
        //sw = sw.add(A);

        double[][]freqArr = A.dfTransform();

        int max = 0;
        for(int i =0; i < freqArr.length; i++){
            if(freqArr[max][1] < freqArr[i][1]){
                max = i;
            }
        }

        System.out.println(freqArr[max][0]);

        //sw.sendToStereoSpeaker();
        //StdPlayer.close();
    }

    /**
     * Appends the given channels to this wave.
     *
     * @param lchannel
     * @param rchannel
     */
    public void append(double[] lchannel, double[] rchannel) {

        for (int i = 0; i < lchannel.length; i++) {

            this.lchannel.add(lchannel[i]);
            this.rchannel.add(rchannel[i]);

        }
    }

    /**
     * Append a wave to this wave.
     *
     * @param other the wave to append.
     */
    public void append(SoundWave other) {
        append(other.getLeftChannel(), other.getRightChannel());
    }

    /**
     * Create a new wave by adding another wave to this wave.
     *
     *
     * @return wave function of current wave + other wave
     * Assuming the lchannel and Rchannel have the same size
     * @parm other the wave to be superpositioned
     */
    public SoundWave add(SoundWave other) {
        double[] biggerLChannel;
        double[] biggerRChannel;
        double[] smallerLChannel;
        double[] smallerRChannel;


        if (other.getRightChannel().length <= this.getRightChannel().length) {
            biggerLChannel = this.getLeftChannel();
            biggerRChannel = this.getRightChannel();

            smallerLChannel = other.getLeftChannel();
            smallerRChannel = other.getRightChannel();

        } else {

            biggerLChannel = other.getLeftChannel();
            biggerRChannel = other.getRightChannel();

            smallerLChannel = this.getLeftChannel();
            smallerRChannel = this.getRightChannel();
        }

        for (int i = 0; i < smallerLChannel.length; i++) {

            biggerLChannel[i] = ampCap(biggerLChannel[i] + smallerLChannel[i]);
            biggerRChannel[i] = ampCap(biggerRChannel[i] + smallerRChannel[i]);
        }
        SoundWave addWave = new SoundWave(biggerLChannel, biggerRChannel);

        return addWave;// change this



    }

    /**
     * clips amplitude to 1
     *
     * @param amp takes amplitude value
     * @return clipamp amplitude in range [-1.0,1.0]
     */
    private double ampCap(double amp) {
        double clipamp = 0.0;
        if (amp > 1.0) {
            clipamp = 1.0;
        } else if (amp < -1.0) {
            clipamp = -1.0;

        } else {
            clipamp = amp;
        }
        return clipamp;
    }
    /**
     * Create a new wave by adding an echo to this wave.
     *
     * @param delta > 0. delta is the lag between this wave and the echo wave.
     * @param alpha > 0. alpha is the damping factor applied to the echo wave.
     * @return a new sound wave with an echo.
     */
    public SoundWave addEcho(int delta, double alpha) {

        double[] echoLeftChannel = new double[this.lchannel.size()+delta];
        double[] echoRightChannel = new double[this.rchannel.size()+delta];

        for (int i = 0; i < echoLeftChannel.length && i < echoRightChannel.length; i++) {
            if (i < delta) {
                echoLeftChannel[i] = 0;
                echoRightChannel[i] = 0;
            } else {
                echoLeftChannel[i] = this.lchannel.get(i - delta);
                echoRightChannel[i] = this.rchannel.get(i - delta);
            }
        }

        SoundWave echoWave = new SoundWave(echoLeftChannel, echoRightChannel);

        echoWave.scale(alpha);

        return this.add(echoWave);

    }

    /**
     * Scale the amplitude of this wave by a scaling factor.
     * After scaling, the amplitude values are clipped to remain
     * between -1 and +1.
     *
     * @param scalingFactor is a value > 0.
     */
    public void scale(double scalingFactor) {
        for(int i = 0; i < lchannel.size() && i < rchannel.size(); i++){
            lchannel.set(i,ampCap(lchannel.get(i) * scalingFactor));
            rchannel.set(i,ampCap(rchannel.get(i) * scalingFactor));
        }
    }

    /**
     * Return a new sound wave that is obtained by applying a high-pass filter to
     * this wave.
     *
     * @param dt >= 0. dt is the time interval used in the filtering process.
     * @param RC > 0. RC is the time constant for the high-pass filter.
     * @return
     */
    public SoundWave highPassFilter(int dt, double RC) {
        double alpha = RC / (RC + dt);
        SoundWave newWave = new SoundWave(this.getLeftChannel(), this.getRightChannel());

        for(int i = 1; i < lchannel.size() && i < rchannel.size(); i++){
            newWave.lchannel.set(i, alpha*newWave.getLeftChannel()[i-1] +
                    alpha * (this.getLeftChannel()[i] - this.getLeftChannel()[i - 1]));

            newWave.rchannel.set(i, alpha*newWave.getRightChannel()[i-1] +
                    alpha * (this.getRightChannel()[i] - this.getRightChannel()[i - 1]));
        }

        return newWave;
    }

    /**
     * Return the frequency of the component with the greatest amplitude
     * contribution to this wave. This component is obtained by applying the
     * Discrete Fourier Transform to this wave.
     *
     * @return the frequency of the wave component of highest amplitude.
     * If more than one frequency has the same amplitude contribution then
     * return the higher frequency.
     */

    public double highAmplitudeFreqComponent() {
        SoundWave wave = new SoundWave(this.getLeftChannel(), this.getRightChannel());
        wave.scale(10000);
        double[][] dfArr = wave.dfTransform();

        int max = 0;
        for(int i = 0 ; i < dfArr.length; i ++){
            if(dfArr[i][1] > dfArr[max][1]){
                max = i;
            }
        }

        return (double) dfArr[max][0]; // change this
    }


    /**
     * Determine if this wave fully contains the other sound wave as a pattern.
     *
     * @param other is the non-zero wave to search for in this wave.
     * @return true if the other wave is contained in this after amplitude scaling,
     * and false if the other wave is not contained in this with any
     * scaling of <=1.0
     *
     */
    public boolean contains(SoundWave other) {
        double[] wl1 = this.getLeftChannel();
        double[] wl2 = other.getLeftChannel();

        double[] wr1 = this.getRightChannel();
        double[] wr2 = other.getRightChannel();

        int length1 = Math.min(wr1.length, wl1.length);
        int length2 = Math.min(wl2.length, wr2.length);


        int i = 0;
        boolean contains = true;
        if (length1 < length2 || length2 ==0) {
            return false;
        }
        while (i <= length1 - length2) {

            if (this.subWave(i, i + length2).similarity(other) ==1.0) {

                    return true;


            }

            i++;


        }
        return false;
    }

    /**
     * Determine the similarity between this wave and another wave.
     * The similarity metric, gamma, is the sum of squares of
     * instantaneous differences.
     *
     * @param other is not null.
     * @return the similarity between this wave and other.
     */
    public double similarity(SoundWave other) {
        double[] wl1 = this.getLeftChannel();
        double[] wl2 = other.getLeftChannel();

        double[] wr1 = this.getRightChannel();
        double[] wr2 = other.getRightChannel();

        double beta =0.0;

        double a=0.0 ,b=0.0 ,c=0.0;

        // Get minimum length out of all waves
        double length = Math.min(Math.min(wr1.length,wr2.length),Math.min(wl1.length,wl2.length));

        // get a ,b ,c
        for(int i=0;i<length;i++){
            a+= Math.pow(wr2[i],2)+ Math.pow(wl2[i],2);
            c += Math.pow(wr1[i],2)+Math.pow(wl1[i],2);
            b += (wl1[i]*wl2[i])+(wr1[i]*wr2[i]);

        }

        beta = b/a;


         double gamma = ( 1/(1+beta*beta*a-2*beta*b+c) );
        beta = b/c;
         gamma+=+ 1/(1+beta*beta*c-2*beta*b+a);


        // get gamma

        return (double)Math.round(gamma*1000000d)/2000000d;
    }

    /**
     * Play this wave on the standard stereo device.
     */
    public void sendToStereoSpeaker() {
        // You may not need to change this...
        double[] lchannel = this.lchannel.stream().mapToDouble(x -> x.doubleValue()).toArray();
        double[] rchannel = this.rchannel.stream().mapToDouble(x -> x.doubleValue()).toArray();
        StdPlayer.playWave(lchannel, rchannel);
    }

    /**
     * Transforms a wave from a time amplitude domain, to a frequency magnitude domain.
     * Correlates the frequency at Arr[i][0] to its strength at Arr[i][1]
     * Note: if the duration of the wave is below 1 second we loose significant accuracy which
     * increases the closer the duration approaches to zero. We can only guarantee accuracy
     * for waves up to +/-0.1 Hz for waves longer than 1 seconds.
     * @return 2D double array representing how strong a frequency is present in a wave
     */
    public double[][] dfTransform(){

        double[][] freqArr = new double[22000][2];
        double[] timeArr = this.getLeftChannel();
        int N = this.lchannel.size();

        int i = 0;
        for(int k = 1; k < freqArr.length; k++){

            ComplexNumbers sumCN = new ComplexNumbers(0,0);

            for(int n = 0; n <  N; n++){

                sumCN = sumCN.sum((new ComplexNumbers(Math.cos((-2*Math.PI*k*n)/N) , Math.sin((-2*Math.PI*k*n)/N))).multiply(timeArr[n]));

            }

            freqArr[i][0] = ((double)k/((double)N/(double)SAMPLES_PER_SECOND));
            freqArr[i][1] = sumCN.magnitude();
            i++;

        }


        return freqArr;
    }

    /**
     * gets the maximum amplitude of the left channel
     * @return left channel maximum amplitude
     */
    public double getAmpLCH(){

        double max = 0;
        double[] lCH = this.getLeftChannel();

        for(double i: lCH){
            if(i > max){
                max = i;
            }
        }

        return max;
    }
    /**
     * gets the maximum amplitude of the right channel
     * @return right channel maximum amplitude
     */
    public double getAmpRCH(){

        double max = 0;
        double[] rCH = this.getRightChannel();

        for(double i: rCH){
            if(i > max){
                max = i;
            }
        }

        return max;
    }

    /**
     * Returns the values of the left and right channel arrays as strings
     * @return
     */
    public String toString(){
        return "lchannel: " + this.lchannel + "\nrchannel: " + this.rchannel;
    }

    /**
     * Creates a subwave from index start to index finish
     * @param start
     * @param finish
     * @return the subwave between the two indexes
     */
    public SoundWave subWave(int start,int finish){

        double[] lchannel = Arrays.copyOfRange(this.getLeftChannel(),start,finish );
        double[] rchannel = Arrays.copyOfRange(this.getRightChannel(),start,finish );

        SoundWave clipped = new SoundWave(lchannel,rchannel);
        return clipped;

    }
}
