import java.io.*;
import java.util.*;
public class kmeans
{
	public static int level=0;
	public static double[][] pointx11=new double[40][1];
	public static double[][] pointy11=new double[40][1];
	public static double[][] pointx00=new double[40][1];
	public static double[][] pointy00=new double[40][1];
	public static int[][] pointx12=new int[40][1];
	public static Random r1 = new Random();
	public static double[] pointx1=new double[40];
	public static double[] pointy1=new double[40];
	public static double[] pointx2=new double[40];
	public static double[] pointy2=new double[40];

  
  
   
	public static  void UpdateCentroid(cluster[] Cluster,int KCount){
		for(int i=0;i<KCount;i++)
		{
			LinkedList<Double> Cluster1=new LinkedList<Double>();
			LinkedList<Double> Cluster2=new LinkedList<Double>();
			double temp1=0,temp2=0;


			for(int j=0;j<100;j++)
			{
				if(Cluster[j].GetClusNum()==i){
					Cluster1.add(Cluster[j].InstCoord[0]);
					Cluster2.add(Cluster[j].InstCoord[1]);
				}
			}
			pointx12[i][0]=Cluster1.size();
			for(int k=0;k<Cluster1.size();k++ )
			{
				temp1=temp1+(double)Cluster1.get(k); 
			}
			for(int k=0;k<Cluster1.size();k++ )
			{
				temp2=temp2+(double)Cluster2.get(k); 
			}
			// calculate the new centroid 
			if(Cluster1.size()!=0)
			{
				pointx1[i]=temp1/(double)Cluster1.size();
				pointy1[i]=temp2/(double)Cluster2.size();
			}
			else if(Cluster1.size()==0){
				pointx1[i]=0;
				pointy1[i]=0;
			}
			pointx11[i][0]=pointx1[i];
			pointy11[i][0]=pointy1[i];  
		}
	}
  public static  double Centroid(double min, double max) {
	  double r2,arId,scan;
	  r2 = max - min;
	  arId = r1.nextDouble() * r2;
	  scan = arId + min;
	  return scan; 
    
  }
  
  public static  double CalculateDist(double x1,double y1,double x2,double y2)
  {
     double x11=0,y11=0,x12=0,y12=0,distance=0;
     x11=(x2-x1);
     y11=(y2-y1);
     x12=Math.pow(x11,2);
     y12=Math.pow(y11,2);
     distance=(Math.sqrt(x12+y12));
     return distance;
  }
  
  public static void CalculateSSE(cluster[] Cluster,int KCount)
  {
  double Dt1=0,Dt2=0,total,finalTotal;int k;
  LinkedList<Double> Cluster4=new LinkedList<Double>();
	for(int i=0;i<KCount;i++)
	{
	 LinkedList<Double> Cluster3=new LinkedList<Double>();
	  for(k=0;k<100;k++){
	  	  	if(Cluster[k].GetClusNum()==i){
	  	  	 double[] temp6=new double[2];
	  	  	 temp6= Cluster[k].GetInstance();
	  	  	if(level==1){
	  	  		Dt1=CalculateDist(pointx00[i][0],pointy00[i][0],temp6[0],temp6[1]);
	  	  		}
	  	    if(level!=1){
	  	    	Dt1=CalculateDist(pointx11[i][0],pointy11[i][0],temp6[0],temp6[1]);
	  	    	}
	  	     Dt2=Math.pow(Dt1,2);
	  	     Cluster3.add(Dt2);
	  	    }
	  	  }
	  	 total=0;
	  	for(int j=0;j<Cluster3.size();j++){
	  		total=total+(double)Cluster3.get(j);
	  		}
	  	Cluster4.add(total);
     }
	finalTotal=0;
   for(int j=0;j<Cluster4.size();j++){
	   finalTotal=finalTotal+(double)Cluster4.get(j);
	   } 
   System.out.println("\n SSE of the Clustering obtained:"+finalTotal);
  }
 public static void main(String[] args) throws FileNotFoundException   
	{
	 File outputfile= new File(args[2]);    // output file 
	 FileOutputStream fStream= new FileOutputStream(outputfile);
	 PrintStream pStream= new PrintStream(fStream);
	 System.setOut(pStream); 	
	 cluster[] Cluster=new cluster[100];
	 int a=0,b=0,d;
	 // array of clusters
	 for(d=0;d<100;d++)
	 {
		 Cluster[d]=new cluster();
	 }
	 int[] arId=new int[100];


	 double[] arX=new double[100];
	 double[] arY=new double[100];
	 Scanner scan2 = null;
	 try {
		 scan2 = new Scanner(new File(args[1]));
	 } catch (FileNotFoundException e) {
		 e.printStackTrace();  
	 }
	 // reading the input file
	 while (scan2.hasNextLine()) {
		 Scanner scan = new Scanner(scan2.nextLine());
		 while (scan.hasNext()) {
			 if(scan.hasNextInt())

			 {
				 arId[a] = scan.nextInt();        // Array of Id column
				 a++;
			 }
			 if(scan.hasNextDouble())
			 { 
				 arX[b] = scan.nextDouble();      // Array for x and y values
				 arY[b] = scan.nextDouble();
				 b++;
			 }
		 }
		 scan.close();
	 }

	 for(int x=0;x<100;x++)
	 {
		 Cluster[x].SetInstance(arId[x],arX[x],arY[x]);
	 }
	 int KCount=Integer.parseInt(args[0]);
	 level=1;
	 if(level==1)
	 {
		 double[] pntx=new double[40];
		 double[] pnty=new double[40];
		 for(int i=0;i<KCount;i++){
			 pntx[i]=Centroid(-0.100,1.100);
			 pnty[i]=Centroid(-0.100,1.100);
			 pointx00[i][0]=pntx[i];
			 pointy00[i][0]=pnty[i];
		 }
		 double[] temp5=new double[2];
		 for(int k=0;k<100;k++)
		 { 
			 temp5=Cluster[k].GetInstance();
			 int g= FindNearestClus(pntx,pnty,temp5[0],temp5[1],KCount);
			 Cluster[k].SetClusNum(g);
		 }
		 int h;
		 for(h=0;h<pntx.length;h++)
		 { pointx1[h]=pntx[h];
		 pointy1[h]=pnty[h];
		 }
	 }
	 l:   for(int h1=0;h1<25;h1++){
		 double[] temp6=new double[2];

		 int ff=0;
		 for(int h=0;h<pointx1.length;h++)
		 { pointx2[h]=pointx1[h];
		 pointy2[h]=pointy1[h];
		 }
		 UpdateCentroid(Cluster,KCount);
		 for(int h=0;h<pointx1.length;h++)
		 { if(pointx2[h]==pointx1[h] && pointy2[h]==pointy1[h])
		 {ff++; }
		 }
		 if(ff==pointx1.length){break l;}

		 for(int k=0;k<100;k++)
		 { 
			 temp6=Cluster[k].GetInstance();
			 int g= FindNearestClus(pointx1,pointy1,temp6[0],temp6[1],KCount);
			 Cluster[k].SetClusNum(g); 
		 }

		 System.out.println("\n Iteration: "+(h1+1));
		 System.out.println("Cluster ID 		List of points ids");
		 for(d=0;d<KCount;d++){
			 int flag=1;
			 System.out.print("      "+(d+1)+"    -----    ");

			 for(int x=0;x<100;x++){
				 if(Cluster[x].GetClusNum()==d){flag=0;
				 System.out.print(Cluster[x].InstanceNum+",");}}

			 if(flag==1){System.out.print("ZERO POINTS IN THIS CLUSTER");}
			 System.out.println();}
	 }

	 CalculateSSE(Cluster,KCount);
  	}
 public static  int FindNearestClus(double[] pntx,double[] pnty,double x,double y,int l2)
 {
 	 double[] Dist=new double[pntx.length];
 	  
 	 double d=0;int j,m,k;
 	 // calculating the dist b/w every centroid and this point 
 	 for(j=0;j<pntx.length;j++)
    {
    	Dist[j]= CalculateDist(pntx[j],pnty[j],x,y);
    }
      d=Dist[0];
      // finding min distance
    for(m=0;m<l2;m++)
    	 {
    	   if(Dist[m]<d){d=Dist[m]; }
    	 }
     for(k=0;k<Dist.length;k++)
     	  {
            if(Dist[k]==d){return k; }      	  
     	  }
     	  return 10;
 }
 
 }
 class cluster{
 
	public int ClusterNum,InstanceNum;
	public double[] InstCoord=new double[2];
	public void SetClusNum(int n){ClusterNum=n;}
	public int GetClusNum(){return ClusterNum;}
	public void SetInstance(int num,double x1,double x2)
	{
	  InstanceNum=num;	
	  InstCoord[0]=x1;
	  InstCoord[1]=x2;
    }
	public double[] GetInstance()
    {
      return (InstCoord);
    }
    public int GetInstanceNum()
    {
       return InstanceNum;
    }
}