package ai.nethra.planner.rag;
import java.util.Locale;
public final class HashingEmbeddingModel {
  public static final int DIMENSIONS=128; private HashingEmbeddingModel(){}
  public static double[] embed(String text){ double[] v=new double[DIMENSIONS]; if(text==null) return v; for(String token: text.toLowerCase(Locale.ROOT).split("[^a-z0-9]+")){ if(token.length()<3) continue; v[Math.floorMod(token.hashCode(),DIMENSIONS)]+=1.0; } double norm=0; for(double x:v) norm+=x*x; norm=Math.sqrt(norm); if(norm>0) for(int i=0;i<v.length;i++) v[i]/=norm; return v; }
  public static double cosine(double[] a,double[] b){ double s=0; for(int i=0;i<Math.min(a.length,b.length);i++) s+=a[i]*b[i]; return s; }
}
