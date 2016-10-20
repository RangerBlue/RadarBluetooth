package com.example.kamil.br;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 07.05.16.
 */
public class MapDraw extends View
{

    private static ArrayList<PathData> data;
    private Paint p;
    private static int number;
    private String TAG = "MapDraw";



    public void setData(ArrayList<PathData> data)
    {
        this.data = data;
    }
    public void setNumber(int number){this.number=number;}

    public MapDraw(Context context)
    {
        super(context);
        setWillNotDraw(false);
    }

    public MapDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public MapDraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public int[] getCornerTime(List<Record> elements)//zwraca 4 wartosci czasu w narożnikach
    {
        int[] corners = new int[5];
        corners[0] = 0;
        int i=1;
        int j=1;
        while( i<elements.size() )
        {
            if(elements.get(i).getEdgeNumber() == 0)   //jesli przerwa
            {
                corners[j] = i;
                j++;
                Log.d("Pozycje narożników", String.valueOf(i));
            }
            i++;
        }

        return corners;
    }

    public long[] getEdgeDuration(int tab[], List<Record> elements)
    {
        long[] edges = new long[4];
        for(int i=0;i<4;i++)
        {
            edges[i] = elements.get(tab[i+1]).getTime()-elements.get(tab[i]).getTime();
            Log.d("Czas trwania krawędzi", String.valueOf(edges[i]));
        }

        return edges;
    }

    public List<String> getNumberOfDevices(List<Record> e)//lista nazw wyszukanych urządzeń
    {
        List<String> names = new ArrayList<String>();
        int i=2;
        int j,k;
        names.add(e.get(1).getName());
        Log.d("dlugosc e", String.valueOf(e.size()));
        while( i<e.size() )//przelatujemy przez wszystkie recordy
        {
            k=0;
            Log.d("Sprawdzenie nazwy", String.valueOf(e.get(i).getName()));
            if(e.get(i).getName()==null)
            {
                Log.d("Sprawdzenie nulla", "null");
            }
            else
            {
                for(j=0;j<names.size();j++)//przelatujemy po wszystkich elementach nazw które wystapiły
                {
                    if( (e.get(i).getName().equals(names.get(j))))
                    {
                        k++; //licznik wystąpień
                    }
                }

                if(k == 0)
                {
                    names.add(e.get(i).getName());//dodajemy nową nazwe
                    Log.d("Add values to name", String.valueOf(e.get(i).getName()));
                }
            }




            Log.d("wartosc k=0", String.valueOf(k));

            Log.d("liczymy pętle", String.valueOf(i));
            i++;
        }

        return names;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        /* warzywne obliczenia
        Random random = new Random();
        //-26 blisko, -100 daleko
        DBHandler db = new DBHandler(getContext());
        //lista
        List<Record> elements = db.getAll();
        List<String> names = getNumberOfDevices(elements);
        Log.d("Ilosc elementów w names", String.valueOf(names.size()));
        int[] corners = getCornerTime(elements);//pozycje narożników w liście
        long[] edges = getEdgeDuration(corners,elements);//czas trwania krawędzi w ms
        //robione na sucho, poprawić żeby było uniwersalnie
        long leftDown = elements.get(corners[0]).getTime();
        long leftUp = elements.get(corners[1]).getTime();
        long rightUp = elements.get(corners[2]).getTime();
        long rightDown = elements.get(corners[3]).getTime();

        long left = edges[0];
        long up = edges[1];
        long right = edges[2];
        long down = edges[3];

        int szerEkranu = getWidth();
        int wysEkranu = getWidth();
        int wys = (int) left;
        int szer = (int) up;

        if(wys>szer)
        {
            szerEkranu = (wysEkranu * szer)/wys;
        }
        else
        {
            wysEkranu = (szerEkranu * wys)/szer;
        }

        //Log.d("obliczenie", String.valueOf(left1*wysEkranu));

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(5);
       // p.setColor(Color.rgb(255, 153, 51));

        //lewa, góra, prawa, dól liczymy od lewej i góry

        for( int w=0; w<names.size(); w++)
        {
            //int randomNum = rand.nextInt((max - min) + 1) + min;
            p.setColor(Color.rgb(random.nextInt((255 - 1) + 1) + 1, random.nextInt((255 - 1) + 1) + 1, random.nextInt((255 - 1) + 1) + 1));

            //ustawienie liczników na dobrą nazwę
            List<Integer> counterNames = new ArrayList<Integer>();
            int z=0;
            Log.d("Element w", String.valueOf(corners[w+1]));
            while(z<corners[1])
            {
                if(names.get(w).equals(elements.get(z).getName()))
                {
                    counterNames.add(z);
                    Log.d("Elementy counterNames", String.valueOf(z));
                }
                z++;
            }

            //lewa
            int zakres=corners[1];
            int licznik=0;
            float stosunek;
            while(licznik<counterNames.size())
            {
                stosunek = (elements.get(counterNames.get(licznik)).getTime()-leftDown)/(float)left;
                canvas.drawLine(0,wysEkranu*stosunek,elements.get(counterNames.get(licznik)).getRssi()*(-1),wysEkranu*stosunek, p);
                licznik++;
            }

            z=0;
            while(z<corners[2])
            {
                if(names.get(w).equals(elements.get(z).getName()))
                {
                    counterNames.add(z);
                    Log.d("Elementy counterNames", String.valueOf(z));
                }
                z++;
            }

            //góra
            zakres=corners[2];
           // licznik=corners[1]+1;
            licznik=0;
            while(licznik<counterNames.size())
            {
                stosunek = (elements.get(counterNames.get(licznik)).getTime()-leftUp)/(float)up;
                canvas.drawLine(szerEkranu*stosunek,0,szerEkranu*stosunek,elements.get(counterNames.get(licznik)).getRssi()*(-1), p);
                licznik++;
            }

            z=0;
            while(z<corners[3])
            {
                if(names.get(w).equals(elements.get(z).getName()))
                {
                    counterNames.add(z);
                    Log.d("Elementy counterNames", String.valueOf(z));
                }
                z++;
            }

            //prawo
            zakres=corners[3];
            //licznik=corners[2]+1;
            licznik=0;
            while(licznik<counterNames.size())
            {
                stosunek = (elements.get(counterNames.get(licznik)).getTime()-rightUp)/(float)right;
                canvas.drawLine(szerEkranu+elements.get(counterNames.get(licznik)).getRssi(),wysEkranu*(stosunek),szerEkranu,wysEkranu*(stosunek), p);
                licznik++;
            }

            z=0;
            while(z<=corners[4])
            {
                if(names.get(w).equals(elements.get(z).getName()))
                {
                    counterNames.add(z);
                    Log.d("Elementy counterNames", String.valueOf(z));
                }
                z++;
            }

            //dół
            zakres=corners[4];
            //licznik=corners[3]+1;
            licznik=0;
            while(licznik<counterNames.size())
            {
                stosunek = (elements.get(counterNames.get(licznik)).getTime()-rightDown)/(float)down;
                canvas.drawLine(szerEkranu*stosunek,wysEkranu+elements.get(counterNames.get(licznik)).getRssi(),szerEkranu*stosunek,wysEkranu, p);
                licznik++;
            }

        }



        Log.d("Rysowanie obwodu", "wyjście z while");


        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setColor(Color.BLUE);
        canvas.drawRect(2,2,szerEkranu-3,wysEkranu-3, p);
        super.onDraw(canvas);
        */




        p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(1);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLUE);

        //dodać obliczanie współczynnika zależnie od urządzenia, gdy jest szersze itp
        int ratio = 44;
        int radius = 3;
        //rysowanie punktów
        for(PathData e : data )
        {
            p.setColor(Color.RED);
            canvas.drawCircle(e.getP1()*ratio,e.getP2Reverse()*ratio,radius,p);
        }


        //rysowanie lini, od pierwszej do ostatniej
        Log.d(TAG, "Number "+Integer.toString(number));
        p.setColor(Color.BLUE);
        for(int i = 0 ; i < data.size()-1; i++)
        {
            if(data.get(i).getEdgeNumber()==number)
            {
                p.setColor(Color.YELLOW);
                canvas.drawLine(data.get(i).getP1()*ratio,data.get(i).getP2Reverse()*ratio,data.get(i+1).getP1()*ratio,data.get(i+1).getP2Reverse()*ratio,p);
            }
            else
            {
                p.setColor(Color.BLUE);
                canvas.drawLine(data.get(i).getP1()*ratio,data.get(i).getP2Reverse()*ratio,data.get(i+1).getP1()*ratio,data.get(i+1).getP2Reverse()*ratio,p);
            }

        }

        //rysowanie lini ostatniej z pierwszą
        if(data.get(data.size()-1).getEdgeNumber()==number)
        {
            p.setColor(Color.YELLOW);
            canvas.drawLine(data.get(data.size()-1).getP1()*ratio,data.get(data.size()-1).getP2Reverse()*ratio,data.get(0).getP1()*ratio,data.get(0).getP2Reverse()*ratio,p);
        }
        else
        {
            p.setColor(Color.BLUE);
            canvas.drawLine(data.get(data.size()-1).getP1()*ratio,data.get(data.size()-1).getP2Reverse()*ratio,data.get(0).getP1()*ratio,data.get(0).getP2Reverse()*ratio,p);
        }


        //canvas.drawLine(10,10,122,13,p);
        //canvas.drawCircle(122,13,2,p);




        super.dispatchDraw(canvas);

    }




    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
