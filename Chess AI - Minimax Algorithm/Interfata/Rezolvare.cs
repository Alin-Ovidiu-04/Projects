using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Drawing;
using System.Linq;
using System.Net.NetworkInformation;

namespace SimpleCheckers
{
    public partial class Board
    {
        /// <summary>
        /// Calculeaza functia de evaluare statica pentru configuratia (tabla) curenta
        /// </summary>
        public double EvaluationFunction()
        {
            double f = 0;

            foreach (var p in Pieces)
            {
                if (p.Player == PlayerType.Computer)
                {
                    f += p.getValue(this);
                }
                else if (p.Player == PlayerType.Human)
                {
                    f -= p.getValue(this);
                }
            }

            return f;
        } 
    }

    //=============================================================================================================================

    public partial class Piece
    {
        /// <summary>
        /// Returneaza lista tuturor mutarilor permise pentru piesa curenta (this)
        /// in configuratia (tabla de joc) primita ca parametru
        /// </summary>
        public List<Move> ValidMoves(Board currentBoard)
        {
            var list = new List<Move>();

            for (int i = 0; i < currentBoard.Size; i++)
            {
                for (int j = 0; j <= currentBoard.Size; j++)
                {
                    Move move = new Move(Id, i, j);
                    if (IsValidMove(currentBoard, move))
                    {
                        list.Add(move);
                    }
                }
            }

            return list;
        }

        /// <summary>
        /// Testeaza daca o mutare este valida intr-o anumita configuratie
        /// </summary>
        public bool IsValidMove(Board currentBoard, Move move)
        {
            if (Id == move.PieceId)
            {
                if (!isOnBoard(move.NewX, move.NewY, currentBoard.Size) || isOccupied(move.NewX, move.NewY, currentBoard) || !isValid(move.NewX, move.NewY, currentBoard))
                    return false;


                Board nextBoard = new Board(currentBoard);
                nextBoard = nextBoard.MakeMove(move);

                if (nextBoard.isOnCheck() == PlayerType.Computer && Player == PlayerType.Computer)
                    return false;

                if (nextBoard.isOnCheck() == PlayerType.Human && Player == PlayerType.Human)
                    return false;
            }

            return true;
        }

        public bool isValid(int x, int y, Board b)
        {
            if (Type == PieceType.Pawn)
            {
                if (Player == PlayerType.Human)
                {
                    if ((y == Y + 1) && (x == X) && !isEnemy(x,y,b))
                        return true;

                    if ((y == Y + 1) && (Math.Abs(x - X) == 1) && isEnemy(x, y, b))
                        return true;
                }
                else
                {
                    if ((y == Y - 1) && (x == X) && !isEnemy(x, y, b))
                        return true;

                    if ((y == Y - 1) && (Math.Abs(x - X) == 1) && isEnemy(x, y, b))
                        return true;
                }
                return false;
            }

            else if(Type == PieceType.Knight)
            {
                if (Math.Abs(y - Y) == 2 && Math.Abs(x - X) == 1)
                    return true;
                if (Math.Abs(y - Y) == 1 && Math.Abs(x - X) == 2)
                    return true;

                return false;
            }

            else if (Type == PieceType.Bishop)
            {
                // verifica daca destinatia este pe diagonala
                if (Math.Abs(x - X) != Math.Abs(y - Y))
                    return false;

                return !hasObstacleOnPath(x, y, b);
            }

            else if (Type == PieceType.Rook)
            {
                // verifica daca destinatia este pe linie 
                if (Math.Abs(x - X) != 0 && Math.Abs(y - Y) != 0)
                    return false;

                return !hasObstacleOnPath(x, y, b);
            }

            else if (Type == PieceType.Queen)
            {
                // verifica daca destinatia este pe linie sau diagonala
                if ((Math.Abs(x - X) != 0 && Math.Abs(y - Y) != 0) && Math.Abs(x - X) != Math.Abs(y - Y))
                    return false;

                return !hasObstacleOnPath(x, y, b);
            }

            else if (Type == PieceType.King)
            {
                int kX = 0, kY = 0;

                foreach (var p in b.Pieces)
                {
                    if(p.Type == PieceType.King && p.Player != Player)
                    {
                        kX = p.X;
                        kY = p.Y;
                    }
                }

                if ((Math.Abs(x - kX) <= 1) && (Math.Abs(y - kY) <= 1))
                    return false;

                //verifica daca urmataorea mutare duce regele in sah sau nu, si il las sa se mute doar daca nu ar fi in sah

                int deltaX = Math.Abs(x - X);
                int deltaY = Math.Abs(y - Y);

                //aici verific si daca nu s-a miscat deloc
                // verific si daca s-a mutat mai mult de o pozitie sau nu (daca deltaX sau deltaY > 1 atunci returneaza false, pentru ca s-ar muta la o distanta de mai multe patrate) 
                return deltaX <= 1 && deltaY <= 1 && (deltaX + deltaY != 0);
            }

            return false;
        }

        public bool isOnBoard(int x, int y, int size)
        {
            return (0 <= x) && (x <= size - 1) && (0 <= y) && (y <= size - 1);
        }

        public bool isOccupied(int x, int y, Board board)
        {
            List<Piece> pieces = board.Pieces;
            foreach (var p in pieces)
            {
                if ((p.X == x) && (p.Y == y))
                {
                    if (p.Player == Player)
                        return true;
                  
                }
            }

            return false;
        }

        public bool hasObstacleOnPath(int destX, int destY, Board b)
        {
            // calculeaza directia in care se misca piesa
            int dirX = Math.Sign(destX - X);
            int dirY = Math.Sign(destY - Y);

            // traversam traseul pas cu pas pentru a detecta obstacole
            int x1 = X + dirX, y1 = Y + dirY;
            while (x1 != destX || y1 != destY)
            {
                if (!isOnBoard(x1, y1, b.Size))
                    return true;

                if (isOccupied(x1, y1, b) || isEnemy(x1, y1, b))
                    return true;

                x1 += dirX;
                y1 += dirY;
            }

            return false;
        }

        public bool isEnemy(int x, int y, Board board)
        {
            List<Piece> pieces = board.Pieces;
            foreach (var p in pieces)
            {
                if (p.X == x && p.Y == y)
                {
                    if (p.Player != Player)
                        return true;
                }
            }
            return false;
        }

        public bool isOnCheck(Board board)
        {
            
            List<Piece> pieces = board.Pieces;

            foreach (var p in pieces)
            {
                if (p.Player != Player && p.Type != PieceType.King && (p.X != X || p.Y != Y))
                {
                    if (p.isValid(X,Y,board))
                    {
                        return true;
                    }
                }
            }

            return false;
        }

        public int getValue(Board b)
        {
            // daca e o piesa eliminata
            if (X == 100)
                return 0;

            switch (Type)
            {
                case PieceType.Pawn:
                    return 1;
                case PieceType.Knight:
                    return 3;
                case PieceType.Bishop:
                    return 3;
                case PieceType.Rook:
                    return 5;
                case PieceType.Queen:
                    return 9;
                case PieceType.King:
                    return 100;
                default:
                    return 0;
            }
        }
    }
    //=============================================================================================================================

    public partial class Minimax
    {
        /// <summary>
        /// Primeste o configuratie ca parametru, cauta mutarea optima si returneaza configuratia
        /// care rezulta prin aplicarea acestei mutari optime
        /// </summary>
        public static Board FindNextBoard(Board currentBoard, PlayerType player, int depth)
        {
            if (depth <= 0)
            {
                return currentBoard;
            }

            Board board = currentBoard;
            List<Board> boards = new List<Board>();
            List<Board> nextBoard = new List<Board>();

            foreach (var p in board.Pieces)
            {
                if (p.Player == player)
                {
                    List<Move> moves = p.ValidMoves(currentBoard);

                    foreach (var m in moves)
                    {
                        boards.Add(currentBoard.MakeMove(m));
                    }
                }
            }

            if (player == PlayerType.Computer)
            {
                double max = -140;
                foreach (var b in boards)
                {
                    double eval = FindNextBoard(b, PlayerType.Human, depth - 1).EvaluationFunction();
                    if (eval >= max)
                    {
                        if (eval == max)
                        {
                            // daca am mai multe configuratii care au functia de evaluare statica maxima, le pun intr-o lista
                            nextBoard.Add(b);
                        }
                        else
                        {
                            // daca gasesc o configuratie care are functia de evaluare statica mai mare decat cele deja testate
                            // -> actualizez maximul, golesc lista de configuratii care au functia de evaluare statica maxima, apoi adaug configuratia gasita in lista 
                            max = eval;
                            nextBoard.Clear();
                            nextBoard.Add(b);
                        }
                    }
                }
            }
            else
            {
                double min = 140;
                foreach (var b in boards)
                {
                    double eval = FindNextBoard(b, PlayerType.Computer, depth - 1).EvaluationFunction();
                    if (eval <= min)
                    {
                        if (eval == min)
                        {
                            // daca am mai multe configuratii care au functia de evaluare statica maxima, le pun intr-o lista
                            nextBoard.Add(b);
                        }
                        else
                        {
                            // daca gasesc o configuratie care are functia de evaluare statica mai mare decat cele deja testate
                            // -> actualizez maximul, golesc lista de configuratii care au functia de evaluare statica maxima, apoi adaug configuratia gasita in lista 
                            min = eval;
                            nextBoard.Clear();
                            nextBoard.Add(b);
                        }
                    }
                }
            }

            if (nextBoard.Count == 0)
                return currentBoard;

            else if (nextBoard.Count == 1)
            {
                // am doar o configuratie in lista, deci ea este singura cu functia de evaluare statica maxima
                Console.WriteLine("Score: " + nextBoard[0].EvaluationFunction());
                return nextBoard[0];
            }
            else
            {
                // am mai multe configuratii care au functia de evaluare statica maxima in lista
                //-> generez un numar random intre 0 si numarul de elemente din lista-1 , apoi returnez elementul din lista aflat la indicele "random"
                Random random = new Random();
                int pos = random.Next(nextBoard.Count);

                Console.WriteLine("Score: " + nextBoard[pos].EvaluationFunction());
                return nextBoard[pos];

            }
        }
    }
}