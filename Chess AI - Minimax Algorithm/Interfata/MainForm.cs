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

using System;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;

namespace SimpleCheckers
{
    public partial class MainForm : Form
    {
        private Board _board;
        private int _selected; // indexul piesei selectate
        private PlayerType _currentPlayer; // om sau calculator

        public MainForm()
        {
            InitializeComponent();

            ImageLoader.Load();

            _board = new Board();
            _currentPlayer = PlayerType.None;
            _selected = -1; // nicio piesa selectata

            this.ClientSize = new System.Drawing.Size(1050, 1000);
            this.pictureBoxBoard.Size = new System.Drawing.Size(700, 700);

            pictureBoxBoard.Refresh();
        }

        private void pictureBoxBoard_Paint(object sender, PaintEventArgs e)
        {
            Bitmap board = new Bitmap(ImageLoader._boardImage);
            e.Graphics.DrawImage(board, 0, 0);

            if (_board == null)
                return;

            int dy = 700 - 120 + 20;

            foreach (Piece p in _board.Pieces)
            {
                Bitmap pieceImage = ImageLoader.GetPieceImage(p.Player, p.Type);

                e.Graphics.DrawImage(pieceImage, 27 + p.X * 82, dy - p.Y * 82, 70, 70);
            }
        }

        private void pictureBoxBoard_MouseUp(object sender, MouseEventArgs e)
        {
            if (_currentPlayer != PlayerType.Human)
                return;

            int mouseX = (e.X-22) / 82;
            int mouseY = 7 - (e.Y-22) / 82;

            if (_selected == -1)
            {
                foreach (Piece p in _board.Pieces.Where(a => a.Player == PlayerType.Human))
                {
                    if (p.X == mouseX && p.Y == mouseY)
                    {
                        _selected = p.Id;
                        pictureBoxBoard.Refresh();
                        break;
                    }
                }
            }
            else
            {
                Piece selectedPiece = _board.Pieces[_selected];

                if (selectedPiece.X == mouseX && selectedPiece.Y == mouseY)
                {
                    _selected = -1;
                    pictureBoxBoard.Refresh();
                }
                else
                {
                    Move m = new Move(_selected, mouseX, mouseY);

                    if (selectedPiece.IsValidMove(_board, m))
                    {
                        _selected = -1;
                        Board b = _board.MakeMove(m);

                        AnimateTransition(_board, b);
                        _board = b;
                        pictureBoxBoard.Refresh();
                        _currentPlayer = PlayerType.Computer;

                        CheckFinish();

                        if (_currentPlayer == PlayerType.Computer) // jocul nu s-a terminat
                            ComputerMove();
                    }
                }
            }

            Console.WriteLine("selected: " + _selected);
        }

        private void ComputerMove()
        {
            Board nextBoard = Minimax.FindNextBoard(_board, _currentPlayer, 3);

            AnimateTransition(_board, nextBoard);
            _board = nextBoard;
            pictureBoxBoard.Refresh();

            _currentPlayer = PlayerType.Human;

            CheckFinish();
        }

        private void CheckFinish()
        {
            bool end; PlayerType winner;
            _board.CheckFinish(_currentPlayer, out end, out winner);

            if (end)
            {
                if (winner == PlayerType.Computer)
                {
                    MessageBox.Show("Calculatorul a castigat!");
                    _currentPlayer = PlayerType.None;
                }
                else if (winner == PlayerType.Human)
                {
                    MessageBox.Show("Ai castigat!");
                    _currentPlayer = PlayerType.None;
                }
            }
        }

        private void AnimateTransition(Board b1, Board b2)
        {
            Bitmap board = new Bitmap(ImageLoader._boardImage);
            int dy = 700 - 120 + 20;
            SolidBrush transparentRed = new SolidBrush(Color.FromArgb(192, 255, 0, 0));
            SolidBrush transparentGreen = new SolidBrush(Color.FromArgb(192, 0, 128, 0));

            Bitmap final = new Bitmap(700, 700);
            Graphics g = Graphics.FromImage(final);

            int noSteps = 10;

            // verificare daca o piesa a fost capturata si eliminarea acesteia din board (mutarea ei la coordonata x = 100)

            //--------------------------------------------------------
            Piece movedPiece = null;
            int eliminated = -1;

            for (int i = 0; i < b1.Pieces.Count; i++)
            {
                if ((b1.Pieces[i].X != b2.Pieces[i].X) || (b1.Pieces[i].Y != b2.Pieces[i].Y))
                {
                    movedPiece = b2.Pieces[i];

                }
            }

            for (int i = 0; i < b2.Pieces.Count; i++)
            {
                if (b2.Pieces[i].X == movedPiece.X && b2.Pieces[i].Y == movedPiece.Y && b2.Pieces[i].Id != movedPiece.Id)
                {
                    eliminated = b2.Pieces[i].Id;
                }
            }

            if (eliminated != -1)
                b2.Pieces[eliminated].X = 100;

            // -------------------------------------------------------


            for (int j = 1; j < noSteps; j++)
            {
                g.DrawImage(board, 0, 0);

                for (int i = 0; i < b1.Pieces.Count; i++)
                {
                    double avx = (j * b1.Pieces[i].X + (noSteps - j) * b1.Pieces[i].X) / (double)noSteps;
                    double avy = (j * b1.Pieces[i].Y + (noSteps - j) * b1.Pieces[i].Y) / (double)noSteps;


                    Bitmap pieceImage = ImageLoader.GetPieceImage(b1.Pieces[i].Player, b1.Pieces[i].Type);

                    g.DrawImage(pieceImage, (int)(27 + avx * 82), (int)(dy - avy * 82), 70, 70);
                }

                Graphics pbg = pictureBoxBoard.CreateGraphics();
                pbg.DrawImage(final, 0, 0);
            }
        }

        private void jocNouToolStripMenuItem_Click(object sender, System.EventArgs e)
        {
            _board = new Board();
            _currentPlayer = PlayerType.Computer;
            ComputerMove();
        }

        private void despreToolStripMenuItem_Click(object sender, EventArgs e)
        {
            const string copyright =
                "Algoritmul minimax\r\n" +
                "(c)Morosanu Alin-Ovidiu,Surdu Tony-Stefanel\r\n";

            MessageBox.Show(copyright, "Despre jocul de sah");
        }

        private void iesireToolStripMenuItem_Click(object sender, System.EventArgs e)
        {
            Environment.Exit(0);
        }
    }
}