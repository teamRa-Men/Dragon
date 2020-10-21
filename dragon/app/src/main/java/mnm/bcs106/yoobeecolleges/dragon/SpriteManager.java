package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.HashMap;

public class SpriteManager {
        Bitmap buildingSheet, NPCSheet, dragonSheet, environmentSheet;
        GameView gameView;
    public SpriteManager() {
        gameView = GameView.instance;
        //buildingSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.buidingSheet);
        //NPCSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.NPCSheet);
        //dragonSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.dragonSheet);
        //environmentSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.environmentSheet);
    }

    private final HashMap<String, Rect> sprites = new HashMap<String, Rect>() {{
        put("Carpet A", new Rect(0, 3, 48, 28));
        put("Carpet B", new Rect(48, 1, 48, 32));
        put("Floor", new Rect(96, 1, 16, 16));

        for(int i = 0; i < sprites.size();i++){

        }
    }};

    private Rect setSelectedSprite(String _spriteName) {
        Rect selectedSprite = sprites.get(_spriteName);
        selectedSprite.right += selectedSprite.left;
        selectedSprite.bottom += selectedSprite.top;
        return selectedSprite;
    }

   /*

    private void drawSpritesInArea(Canvas _canvas, String _sprite, int _originX, int _originY, int _repeatsX, int _repeatsY) {
        Rect selectedSprite = setSelectedSprite(_sprite);

        for (int y = 0; y < _repeatsY; y++) {
            for (int x = 0; x < _repeatsX; x++) {
                int left = _originX + ((selectedSprite.width() * scale) * x);
                int right = left + (selectedSprite.width() * scale);
                int top = _originY + ((selectedSprite.height() * scale) * y);
                int bottom = top + (selectedSprite.height() * scale);

                Rect destinationArea = new Rect(left, top, right, bottom);
                _canvas.drawBitmap(bitmap, selectedSprite, destinationArea, null);
            }
        }
    }

    private void drawSpritesFromCentre(Canvas _canvas, String _sprite, int _originX, int _originY, int _repeatsX, int _repeatsY) {
        Rect selectedSprite = setSelectedSprite(_sprite);

        for (int y = 0; y < _repeatsY; y++) {
            for (int x = 0; x < _repeatsX; x++) {
                int left = ((getWidth() / 2) + _originX - ((scale * selectedSprite.width()) * (_repeatsX / 2))) + ((selectedSprite.width() * scale) * x) - (_repeatsX % 2 == 1 ? ((selectedSprite.width() * scale) / 2) : 0);
                int right = left + (selectedSprite.width() * scale);
                int top = ((getHeight() / 2) + _originY - ((scale * selectedSprite.height()) * (_repeatsY / 2))) + ((selectedSprite.height() * scale) * y) - (_repeatsY % 2 == 1 ? ((selectedSprite.height() * scale) / 2) : 0);
                int bottom = top + (selectedSprite.height() * scale);

                Rect destinationArea = new Rect(left, top, right, bottom);
                _canvas.drawBitmap(bitmap, selectedSprite, destinationArea, null);
            }
        }
    }
}
*/
}
