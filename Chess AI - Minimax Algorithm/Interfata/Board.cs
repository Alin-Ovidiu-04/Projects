/**************************************************************************
 *                                                                        *
 *  Copyright:   (c) 2016-2020, Florin Leon                               *
 *  E-mail:      florin.leon@academic.tuiasi.ro                           *
 *  Website:     http://florinleon.byethost24.com/lab_ia.html             *
 *  Description: Game playing. Minimax algorithm                          *
 *               (Artificial Intelligence lab 7)                          *
 *                                                                        *
 *  This code and information is provided "as is" without warranty of     *
 *  any kind, either expressed or implied, including but not limited      *
 *  to the implied warranties of merchantability or fitness for a         *
 *  particular purpose. You are free to use this source code in your      *
 *  applications as long as the original copyright notice is included.    *
 *                                                                        *
 **************************************************************************/

using System.Collections.Generic;
using System.Linq;

namespace SimpleCheckers
{
    /// <summary>
    /// Reprezinta o configuratie a jocului (o tabla de joc) la un moment dat
    /// </summary>
    public partial class Board
    {
        public int Size { get; set; } // dimensiunea tablei de joc
        public List<Piece> Pieces { get; set; } // lista de piese, atat ale omului cat si ale calculatorului

        public Board()
        {
            Size = 8;
            Pieces = new List<Piece>(Size * 4);

            for (int i = 0; i < Size; i++)
            {
                PieceType Type = PieceType.Pawn;

                switch(i)
                {
                    case 0:
                        Type = PieceType.Rook; break;
                    case 1:
                        Type = PieceType.Knight; break;
                    case 2:
                        Type = PieceType.Bishop; break;
                    case 3:
                        Type = PieceType.King; break;
                    case 4:
                        Type = PieceType.Queen; break;
                    case 5:
                        Type = PieceType.Bishop; break;
                    case 6:
                        Type = PieceType.Knight; break;
                    case 7:
                        Type = PieceType.Rook; break;
                }
                
                Pieces.Add(new Piece(i, Size - 1, i, PlayerType.Computer, Type));
            }

            for (int i = 0; i < Size; i++)
                Pieces.Add(new Piece(i, Size - 2, i + Size, PlayerType.Computer, PieceType.Pawn));

            for (int i = 0; i < Size; i++)
            {
                PieceType Type = PieceType.Pawn;

                switch (i)
                {
                    case 0:
                        Type = PieceType.Rook; break;
                    case 1:
                        Type = PieceType.Knight; break;
                    case 2:
                        Type = PieceType.Bishop; break;
                    case 3:
                        Type = PieceType.King; break;
                    case 4:
                        Type = PieceType.Queen; break;
                    case 5:
                        Type = PieceType.Bishop; break;
                    case 6:
                        Type = PieceType.Knight; break;
                    case 7:
                        Type = PieceType.Rook; break;
                }

                Pieces.Add(new Piece(i, 0, i + 2 * Size, PlayerType.Human, Type));
            }

            for (int i = 0; i < Size; i++)
                Pieces.Add(new Piece(i, 1, i + 3 * Size, PlayerType.Human, PieceType.Pawn));
        }

        public Board(Board b)
        {
            Size = b.Size;
            Pieces = new List<Piece>(Size * 2);

            foreach (Piece p in b.Pieces)
                Pieces.Add(new Piece(p.X, p.Y, p.Id, p.Player, p.Type));
        }

        // public double EvaluationFunction() - completati aceasta metoda in fisierul Rezolvare.cs

        /// <summary>
        /// Creeaza o noua configuratie aplicand mutarea primita ca parametru in configuratia curenta
        /// </summary>
        public Board MakeMove(Move move)
        {
            Board nextBoard = new Board(this); // copy

            // elimina piesa de pe pozitia noua daca este cazul
            foreach (Piece p in nextBoard.Pieces)
            {
                if (p.X == move.NewX && p.Y == move.NewY)
                {
                    nextBoard.Pieces[p.Id].X = 100;
                }

                // schimb pion in regina
                if (p.Type == PieceType.Pawn)
                {
                    if ((p.Player == PlayerType.Human && p.Y == Size-1) || (p.Player == PlayerType.Computer && p.Y == 0))
                    {
                        p.Type = PieceType.Queen;
                    }
                }
            }

            // executa mutarea
            nextBoard.Pieces[move.PieceId].X = move.NewX;
            nextBoard.Pieces[move.PieceId].Y = move.NewY;

            return nextBoard;
        }

        /// <summary>
        /// Verifica daca configuratia curenta este castigatoare
        /// </summary>
        /// <param name="finished">Este true daca cineva a castigat si false altfel</param>
        /// <param name="winner">Cine a castigat: omul sau calculatorul</param>
        public void CheckFinish(PlayerType player, out bool finished, out PlayerType winner)
        {
            finished = false;
            winner = PlayerType.None;

            if (player == PlayerType.Human)
            {
                finished = true;
                winner = PlayerType.Computer;

                foreach (Piece p in Pieces)
                {
                    if (p.Player == PlayerType.Human && p.ValidMoves(this).Count != 0)
                    {
                        finished = false;
                        winner = PlayerType.None;
                        break;
                    }
                }
            }

            if (finished) return;

            if (player == PlayerType.Computer)
            {
                finished = true;
                winner = PlayerType.Human;

                foreach (Piece p in Pieces)
                {
                    if (p.Player == PlayerType.Computer && p.ValidMoves(this).Count != 0)
                    {
                        finished = false;
                        winner = PlayerType.None; 
                        break;
                    }
                }
            }
        }

        public PlayerType isOnCheck()
        {
            Piece whiteKing = null, blackKing = null;
            foreach (Piece piece in  Pieces)
            {
                if (piece.Type == PieceType.King)
                {
                    if (piece.Player == PlayerType.Computer) whiteKing = piece;
                    else blackKing = piece;
                }
            }

            if (whiteKing.isOnCheck(this))
                return PlayerType.Computer;
            else if (blackKing.isOnCheck(this))
                return PlayerType.Human;

            return PlayerType.None;
        }
    }
}