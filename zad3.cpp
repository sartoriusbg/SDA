//#include<iostream>
//#include<string>
//using namespace std;
//int numberOfSimbol(string word, char simbol)
//{
//	int result=0;
//	for (int i = 0; i < word.size(); i++)
//	{
//		if (word[i] == simbol)
//		{
//			result++;
//		}
//	}
//	return result;
//}
//int solution(string word, int number, char simbol)
//{
//	int result = 0;
//	result = (number / word.size()) * numberOfSimbol(word, simbol) + numberOfSimbol(word.substr(0, number % word.size()),simbol);
//	return result;
//}
//int main()
//{
//	string word;
//	int number;
//	char simbol;
//	cin >> word;
//	cin >> number;
//	cin >> simbol;
//	cout << solution(word, number, simbol);
//	return 0;
//}