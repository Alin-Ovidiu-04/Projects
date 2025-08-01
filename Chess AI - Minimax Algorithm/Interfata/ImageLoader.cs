using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SimpleCheckers
{
    public static class ImageLoader
    {
        public static Bitmap _boardImage;
        public static Bitmap _wPawnImage, _wKnightImage, _wBishopImage, _wRookImage, _wKingImage, _wQueenImage;
        public static Bitmap _bPawnImage, _bKnightImage, _bBishopImage, _bRookImage, _bKingImage, _bQueenImage;

        public static void Load()
        {
            try
            {
                // board
                _boardImage = (Bitmap)Image.FromFile("chess3.jpg");

                // white pieces
                _wPawnImage = (Bitmap)Image.FromFile("pieces/w_pawn_png_shadow_128px.png");
                _wKnightImage = (Bitmap)Image.FromFile("pieces/w_knight_png_shadow_128px.png");
                _wBishopImage = (Bitmap)Image.FromFile("pieces/w_bishop_png_shadow_128px.png");
                _wRookImage = (Bitmap)Image.FromFile("pieces/w_rook_png_shadow_128px.png");
                _wKingImage = (Bitmap)Image.FromFile("pieces/w_king_png_shadow_128px.png");
                _wQueenImage = (Bitmap)Image.FromFile("pieces/w_queen_png_shadow_128px.png");

                // black pieces
                _bPawnImage = (Bitmap)Image.FromFile("pieces/b_pawn_png_shadow_128px.png");
                _bKnightImage = (Bitmap)Image.FromFile("pieces/b_knight_png_shadow_128px.png");
                _bBishopImage = (Bitmap)Image.FromFile("pieces/b_bishop_png_shadow_128px.png");
                _bRookImage = (Bitmap)Image.FromFile("pieces/b_rook_png_shadow_128px.png");
                _bKingImage = (Bitmap)Image.FromFile("pieces/b_king_png_shadow_128px.png");
                _bQueenImage = (Bitmap)Image.FromFile("pieces/b_queen_png_shadow_128px.png");
            }
            catch
            {
                MessageBox.Show("Nu se poate incarca o imagine");
                Environment.Exit(1);
            }
        }

        public static Bitmap GetPieceImage(PlayerType playerType, PieceType pieceType)
        {
            Bitmap pieceImage = null;

            // black pieces
            if (playerType == PlayerType.Human)
            {
                switch (pieceType)
                {
                    case PieceType.Pawn:
                        pieceImage = _bPawnImage;
                        break;

                    case PieceType.Knight:
                        pieceImage = _bKnightImage;
                        break;

                    case PieceType.Bishop:
                        pieceImage = _bBishopImage;
                        break;

                    case PieceType.Rook:
                        pieceImage = _bRookImage;
                        break;

                    case PieceType.King:
                        pieceImage = _bKingImage;
                        break;

                    case PieceType.Queen:
                        pieceImage = _bQueenImage;
                        break;
                }
            }
            // white pieces
            else if (playerType == PlayerType.Computer)
            {
                switch (pieceType)
                {
                    case PieceType.Pawn:
                        pieceImage = _wPawnImage;
                        break;

                    case PieceType.Knight:
                        pieceImage = _wKnightImage;
                        break;

                    case PieceType.Bishop:
                        pieceImage = _wBishopImage;
                        break;

                    case PieceType.Rook:
                        pieceImage = _wRookImage;
                        break;

                    case PieceType.King:
                        pieceImage = _wKingImage;
                        break;

                    case PieceType.Queen:
                        pieceImage = _wQueenImage;
                        break;
                }
            }

            return pieceImage;
        }
    }
}
