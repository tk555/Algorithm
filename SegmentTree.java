package com.tk555.algorithm;

public abstract class SegmentTree {

    public static void put(Object object){
        System.out.println(object);
    }
    public static String format(String string,Object... args){
        return String.format(string,args);
    }

    public static void main(String[] args) {
        long[] nums = {3,4,2,2,5,1,1,4,2,2,-4,-2,-1,0,0,0,0,0};
        SumSegmentTree st = new SumSegmentTree(nums);
        MinSegmentTree minSt=new MinSegmentTree(nums);
        MaxSegmentTree maxSt=new MaxSegmentTree(nums);
        put(st);
        put(minSt);
        put(maxSt);
    }
    protected final int originSize;
    protected final int height;
    protected final long[] node;
    protected final long noRelationVal;

    protected SegmentTree (long[] original,long noRelationVal){
        this.noRelationVal=noRelationVal;
        originSize = original.length;
        {
            int height_ = 1;
            while (originSize > (1 << (height_ - 1))) {
                height_++;
            }
            height = height_;
        }
        this.node = new long[ (1<<height)-1];
        //まず元の配列に対応する部分のnodeを埋める
        for(int i=0;i<originSize;i++){
            this.node[(1<<(height-1))-1+i]=original[i];
        }
        for(int i=(1<<(height-1))-1+originSize;i<(1<<height)-1;i++){
            this.node[i]=noRelationVal;
        }
        //空いているnodeを後ろから埋めていく
        for(int i = (1<<(height-1))-2; i>=0; i--){
            this.node[i]=marge(this.node[i*2+1],this.node[i*2+2]);
        }
    }
    public long getVal(int a,int b){
        return getVal(a,b,0,0,-1);
    }
    public long getVal(int a, int b, int k, int l, int r) {
        //node[k]=origin上の範囲[l,r]の合計を表している
        put(format("%d,%d,%d,%d,%d",a,b,k,l,r));
        if(r < 0) r = (1<<(height-1))-1;
        if(b<l||r<a) return noRelationVal;
        if(a <= l && r <= b) return node[k];

        long vl = getVal(a, b, 2*k+1, l, (l+r)/2);
        long vr = getVal(a, b, 2*k+2, (l+r)/2+1, r);
        return marge(vl,vr);
    }
    public long getVal(int index){
        return node[(1<<(height-1))-1+index];
    }

    void setVal(int index,long val){
        if(index<0||index>=originSize) throw new IllegalArgumentException(format("挿入位置が不正%d",index));
        index=(1<<(height-1))-1+index;
        node[index]=val;
        while(index>0){
            index=(index-1)/2;
            node[index]=marge(node[2*index+1],node[2*index+2]);
        }
    }
    public abstract long marge(long a,long b);
    @Override
    public String toString(){
        StringBuilder result=new StringBuilder();
        result.append(format("Class:%s\n",getClass().getSimpleName()));
        result.append(format("height:%d\n",height));
        for(int currentHeight=1;currentHeight<=height;currentHeight++){
            for(int i=(1<<(currentHeight-1))-1;i<=(1<<currentHeight)-2;i++){
                result.append(format("%d ",node[i]));
            }
            result.append("\n");
        }
        return result.toString();
    }


    public static final class SumSegmentTree extends SegmentTree {
        SumSegmentTree(long[] original){
            this(original,0);
        }
        SumSegmentTree(long[] original,long noRelationVal){
            super(original,noRelationVal);
        }

        @Override
        public long marge(long a, long b) {
            return a+b;
        }
    }
    public static final class MinSegmentTree extends SegmentTree {
        MinSegmentTree(long[] original){
            this(original,Long.MAX_VALUE);
        }
        MinSegmentTree(long[] original,long noRelationVal){
            super(original,noRelationVal);
        }

        @Override
        public long marge(long a, long b) {
            return Math.min(a,b);
        }

    }
    public static final class MaxSegmentTree extends SegmentTree {
        MaxSegmentTree(long[] original){
            this(original,-Long.MIN_VALUE);
        }
        MaxSegmentTree(long[] original,long noRelationVal){
            super(original,noRelationVal);
        }

        @Override
        public long marge(long a, long b) {
            return Math.max(a,b);
        }
    }
}
